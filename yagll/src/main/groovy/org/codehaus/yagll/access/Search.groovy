package org.codehaus.yagll.access

import javax.naming.directory.SearchControls
import org.codehaus.yagll.context.*

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 18, 2009
 * Time: 9:29:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class Search {

  static List search(factory, entity, searchRoot, filter = "objectclass=*", scope = SearchControls.SUBTREE_SCOPE) {
    def clazz = factory.class
    def searchControls = new SearchControls()
    searchControls.setSearchScope(scope)
    def matchingEntries = LdapUtils.search(searchRoot, filter, searchControls)
    def results = []
    matchingEntries.each {expando ->
      def instance = null
      try {
        instance = clazz.newInstance()
      } catch (e) {
        instance = factory.newInstance()
      }
      if (populateInstanceFromExpando(instance, expando, entity)) {
        if (instance.properties != [:]) {
          results << instance
        }
      }
    }
    results
  }

  static boolean populateInstanceFromExpando(instance, expando, entity) {
    def returnValue = false
    def declaredFields = instance.class.declaredFields
    declaredFields.each {field ->
      def annotations = field.declaredAnnotations
      annotations.each {annotation ->
        if (annotation.annotationType().name == "org.codehaus.yagll.annotation.DirectoryEntity") {
          if (entity == annotation.entity()) {
            def attribute = annotation.attribute()
            if (expando[attribute]) {
              try {
                instance[field.name] = field.type.newInstance()
              } catch (e) {
                instance[field.name] = instance.fieldFactory(field.name)
              }
              if (instance[field.name] instanceof Collection) {
                expando[attribute].each {
                  instance[field.name].add(it)
                }
              } else {
                instance[field.name] = expando[attribute].first()
              }
              returnValue = true
            }
          }
        }
      }
    }
    returnValue
  }

}
