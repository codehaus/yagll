package org.codehaus.yagll.access

import org.codehaus.yagll.context.*


/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 3:32:11 AM
 * To change this template use File | Settings | File Templates.
 */

public class Lookup {

  static lookup(object, entityToNameHashtable) {
    def found = false
    def declaredFields = object.class.declaredFields
    declaredFields.each {field ->
      def annotations = field.declaredAnnotations
      annotations.each {annotation ->
        if (annotation.annotationType().name == "org.codehaus.yagll.annotation.DirectoryEntity") {
          def entity = annotation.entity()
          def attribute = annotation.attribute()
          String name = entityToNameHashtable[entity]
          if (name) {
            def values = LdapUtils.getCachedOrSearch(name)
            if (values && values[attribute]) {
              found = true
              try {
                object[field.name] = field.type.newInstance()
              } catch (e) {
                object[field.name] = object.fieldFactory(field.name)
              }
              if (object[field.name] instanceof Collection) {
                values[attribute].each {
                  object[field.name].add(it)
                }
              } else {
                object[field.name] = values[attribute].first()
              }
            }
          }
        }
      }
    }
    found ? object : null
  }

}
