package org.devicesoft.yagll.context

import javax.naming.directory.InitialDirContext
import javax.naming.directory.DirContext
import javax.naming.directory.SearchControls
import javax.naming.directory.ModificationItem
import javax.naming.NameNotFoundException
import javax.naming.directory.BasicAttribute
import javax.naming.directory.BasicAttributes
import org.devicesoft.yagll.Yagll
import javax.naming.ldap.LdapName

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Feb 27, 2009
 * Time: 4:54:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class LdapUtils {

  static cache = [:]

  static InitialDirContext getInitialDirContext() {
    Yagll.authenticator.getInitialDirContext()
  }

  static expandoFromName(String name) {
    def result = new Expando()
    def ctx = getInitialDirContext()
    def searchControls = new SearchControls()
    searchControls.setSearchScope(SearchControls.OBJECT_SCOPE)
    def filter = "objectclass=*"
    def searchResults = null
    try {
      searchResults = ctx.search(name, filter, searchControls)
    } catch (NameNotFoundException e) {
    }
    while (searchResults?.hasMore()) {
      def searchResult = searchResults.next()
      def attributes = searchResult?.attributes
      def namingEnum = attributes?.getAll()
      while (namingEnum?.hasMore()) {
        def attribute = namingEnum.next()
        def allValues = attribute.getAll()
        result[attribute.ID] = []
        while (allValues?.hasMore()) {
          def value = allValues.next()
          try {
              if (value.class == String)
                  result[attribute.ID] << new String(value)
              else if (value.class == byte[])
                result[attribute.ID] << value
          } catch (ClassCastException e1) {
              result[attribute.ID] << new String(value)
          }
        }
      }
      result["=="] = [searchResult.nameInNamespace]
      result["="] = [new LdapName(searchResult.nameInNamespace).getSuffix(new LdapName(ctx.nameInNamespace).rdns.size()).toString()]
    }
    return result
  }

  static getCachedOrSearch(name) {
    def result = null

    if (Yagll.cache) {
      result = Yagll.cache.lookup(name)
    }
    if (!result) {
      result = expandoFromName(name)
      if (Yagll.cache) {
        Yagll.cache.bind(name, result)
      }
    }
    result
  }

  static void bindEntity(String name, List objectClasses, Expando values) {
    def ocAttrs = new BasicAttribute("objectclass")
    objectClasses.each { ocAttrs.add(it) }

    def attrs = new BasicAttributes()
    values.properties.each { attrID, val ->
      def multivalue = new BasicAttribute(attrID)
      val.each {
        multivalue.add(it)
      }
      attrs.put(multivalue)
    }
    attrs.put(ocAttrs)

    def ctx = getInitialDirContext()
    ctx.bind(name, null, attrs)
  }

  static void updateEntity(String name, mods) {
    def cardinality = [:]
    def modsAsStrings = []
    
    mods.each {
      def str
      def expando = new Expando(mod: it)
      if (it.modificationOp == DirContext.ADD_ATTRIBUTE) {
        str = it.toString() - "Add "
      } else if (it.modificationOp == DirContext.REMOVE_ATTRIBUTE) {
        str = it.toString() - "Remove "
      } else {
        str = it.toString()
      }
      expando.str = str
      modsAsStrings << expando

      if (cardinality.containsKey(str)) {
        cardinality[str]++
      } else {
        cardinality[str] = 1
      }
    }

    modsAsStrings.each {
      if (cardinality[it.str] > 1) {
        mods.remove(it.mod)
      }
    }

    def ctx = getInitialDirContext()

    mods.each {
      ctx.modifyAttributes(name, [it] as ModificationItem[])
    }
  }

  static List search(searchRoot, filter, scope) {
    def results = []
    def ctx = getInitialDirContext()
    def searchResults = null
    try {
      searchResults = ctx.search(searchRoot, filter, scope)
    } catch (NameNotFoundException e) {
    }
    while (searchResults?.hasMore()) {
      def result = new Expando()
      def searchResult = searchResults.next()
      def attributes = searchResult?.attributes
      def namingEnum = attributes?.getAll()
      while (namingEnum?.hasMore()) {
        def attribute = namingEnum.next()
        def allValues = attribute.getAll()
        result[attribute.ID] = []
        while (allValues?.hasMore()) {
          result[attribute.ID] << allValues.next()
        }
      }
      result["=="] = [searchResult.nameInNamespace]
      result["="] = [new LdapName(searchResult.nameInNamespace).getSuffix(new LdapName(ctx.nameInNamespace).rdns.size()).toString()]
      results << result
    }
    results
  }

  static void unbind(name) {
    def ctx = getInitialDirContext()
    ctx.unbind(name)
  }

}