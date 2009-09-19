package org.devicesoft.yagll.access
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 3:32:23 AM
 * To change this template use File | Settings | File Templates.
 */

import org.devicesoft.yagll.context.LdapUtils
import javax.naming.directory.ModificationItem
import javax.naming.directory.DirContext
import javax.naming.directory.BasicAttribute
import javax.naming.ldap.LdapName
import javax.naming.directory.BasicAttributes

public class Bind {

  static stringByteMap = [:]
  static refCount = 0

  static getBytes(str) {
    if (!stringByteMap.containsKey(str)) {
      stringByteMap[str] = str.getBytes()
    }
    stringByteMap[str]
  }

  static void bind(object, HashMap entityToNameHashtable) {

    refCount++

    try {
      def currentValues = [:]

      entityToNameHashtable.values().each {String name ->
        currentValues[name] = LdapUtils.getCachedOrSearch(name)
        tagUniqueFields(object, currentValues[name])
        convertToByteArrays(object, currentValues[name])
      }

      currentValues.each {String name, Expando values ->
        entityToNameHashtable.each {String entity, String dn ->
          def props = values.properties
          props.remove("=uniqueKeys")

          if (props.isEmpty()) {
            if (dn == name) {
              def vals = getValues(entity, object)
              def objectClasses = getObjectClasses(entity, object)
              if (!vals.properties.isEmpty() && objectClasses) {
                LdapUtils.bindEntity(name, objectClasses, vals)
              }
            }
          }
          else {
            def expando = getValues(entity, object)
            def mods = getModificationItems(values, expando)
            if (mods) {
              LdapUtils.updateEntity(name, mods)
            }
          }
        }
      }
    } catch (e) {
      refCount--

      if (refCount == 0) {
        stringByteMap = [:]
      }
      throw e
    }

    refCount--

    if (refCount == 0) {
      stringByteMap = [:]
    }

  }

  static void tagUniqueFields(object, expando) {
    expando["=uniqueKeys"] = expando.properties.keySet()
  }

  static void convertToByteArrays(object, expando) {
    object.class.declaredFields.each {field ->
      field.declaredAnnotations.each {annotation ->
        if (annotation.annotationType().name == "org.devicesoft.yagll.annotation.DirectoryEntity") {
          if (annotation.isByteArray()) {
            def values = []
            expando[annotation.attribute()].each {val ->
              if (val instanceof String) {
                values << getBytes(val)
              } else {
                values << val
              }
            }
            if (values) {
              expando[annotation.attribute()] = values as Set
            }
          }
        }
      }
    }
  }

  static List getObjectClasses(String entity, object) {
    def objectClasses = []

    object.class.declaredAnnotations.each {annotation ->
      if (annotation.annotationType().name == "org.devicesoft.yagll.annotation.DirectoryBound") {
        def entities = annotation.entities() as List
        def idx = entities.indexOf(entity)
        if (idx > -1) {
          if (idx < annotation.objectClasses().length) {
            def objectClassStr = annotation.objectClasses()[idx]
            objectClasses = objectClassStr.split(", *") as List
          }
        }
      }
    }
    objectClasses
  }

  static Expando getValues(String entity, object) {
    def result = new Expando()

    object.class.declaredFields.each {field ->
      if (!object[field.name]) {
        return
      }
      field.declaredAnnotations.each {annotation ->
        if (annotation.annotationType().name == "org.devicesoft.yagll.annotation.DirectoryEntity") {
          if (entity == annotation.entity()) {
            if (annotation.attribute().startsWith("=")) {
              // skip these fields, cannot change the name in namespace this way
            } else if (annotation.mustBeUnique()) {
              if (object[field.name] instanceof Collection) {
                result[annotation.attribute()] = [] as Set
                object[field.name].unique {a, b -> a != b ? 1 : 0 }.each {
                  if (annotation.isByteArray()) {
                    result[annotation.attribute()] << getBytes(it)
                  } else {
                    result[annotation.attribute()] << it
                  }
                }
              } else if (annotation.isByteArray()) {
                result[annotation.attribute()] = [getBytes(object[field.name])] as Set
              } else {
                result[annotation.attribute()] = [object[field.name]] as Set
              }
            } else if (object[field.name] instanceof Collection) {
              result[annotation.attribute()] = object[field.name]
            } else {
              result[annotation.attribute()] = [object[field.name]]
            }
          }
        }
      }
    }
    result
  }

  static Expando getModifiedValues(String entity, object) {
    def result = new Expando()

    object.class.declaredFields.each {field ->
      if (!object[field.name]) {
        return
      }
      field.declaredAnnotations.each {annotation ->
        if (annotation.annotationType().name == "org.devicesoft.yagll.annotation.DirectoryEntity") {
          if (entity == annotation.entity()) {
            if (annotation.attribute().startsWith("=")) {
              // skip these fields, cannot change the name in namespace this way
            } else if (annotation.mustBeUnique()) {
              if (object[field.name] instanceof Collection) {
                result[annotation.attribute()] = [] as Set
                object[field.name].unique {a, b -> a != b ? 1 : 0 }.each {
                  if (annotation.isByteArray()) {
                    result[annotation.attribute()] << getBytes(it)
                  } else {
                    result[annotation.attribute()] << it
                  }
                }
              } else if (annotation.isByteArray()) {
                result[annotation.attribute()] = [getBytes(object[field.name])]
              } else {
                result[annotation.attribute()] = [object[field.name]]
              }
            } else if (annotation.isByteArray()) {
              if (object[field.name] instanceof Collection) {
                result[annotation.attribute()] = object[field.name].collect { getBytes(it) } as Set
              } else {
                result[annotation.attribute()] = [getBytes(object[field.name])]
              }
            } else if (!(object[field.name] instanceof Collection)) {
              result[annotation.attribute()] = [object[field.name]]
            }
            else {
              result[annotation.attribute()] = object[field.name]
            }
          }
        }
      }
    }
    result
  }

  static getModificationItems(curr, trgt) {

    def modificationItems = []

    def current = new Expando()
    def target = new Expando()
    def uniqueKeys = []

    curr.properties.each {k, v ->
      if (!k.startsWith("=") && k.compareToIgnoreCase("objectclass") != 0) {
        current[k] = v
      }
    }

    trgt.properties.each {k, v ->
      if (!k.startsWith("=") && k.compareToIgnoreCase("objectclass") != 0) {
        target[k] = v
      }
    }

    trgt["=uniqueKeys"].each {k ->
      if (!k.startsWith("=") && k.compareToIgnoreCase("objectclass") != 0) {
        uniqueKeys << k
      }
    }

    def replaceKeys = current.properties.keySet().findAll { current[it] && target[it] }
    def removeKeys = current.properties.keySet().findAll { current[it] } - replaceKeys
    def addKeys = target.properties.keySet().findAll { target[it] } - replaceKeys

    replaceKeys.each {String key ->
      if ((current[key].intersect(target[key]).size() * 2 ) != (current[key].size() + target[key].size())) {
        def attr = new BasicAttribute(key)
        target.getProperty(key).each {
          if (it instanceof byte[]) {
            attr.add(it)
          } else {
            attr.add(it as String)
          }
        }
        def mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr)
        modificationItems << mod
      }
    }

    removeKeys.each {String key ->
      current[key].each {Object val ->
        def attr = new BasicAttribute(key, val)
        def mod = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr)
        modificationItems << mod
      }
    }

    addKeys.each {String key ->
      target[key].each {Object val ->
        def attr = new BasicAttribute(key, val)
        def mod = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr)
        modificationItems << mod
      }
    }

    //modificationItems.addAll(getReplaceModificationItems(replaceKeys, uniqueKeys, current, target))

    modificationItems

  }

  static getReplaceModificationItems(keys, uniqueKeys, current, target) {

    def modificationItems = []

    keys.each {key ->
      def isUniqueKey = uniqueKeys.contains(key)
      modificationItems.addAll(getReplaceModificationItemsForSingleKey(key, isUniqueKey, current, target))
    }

    modificationItems

  }

  static getReplaceModificationItemsForSingleKey(String key, boolean isUniqueKey, Expando current, Expando target) {

    def modificationItems = []

    target[key].each {Object val ->
      def attr = new BasicAttribute(key, val)
      def mod = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr)
      modificationItems << mod
    }

    current[key].each {Object val ->
      def attr = new BasicAttribute(key, val)
      def mod = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr)
      modificationItems << mod
    }

    modificationItems

  }

}