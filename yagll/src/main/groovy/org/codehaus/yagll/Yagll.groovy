package org.codehaus.yagll
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 23, 2009
 * Time: 9:58:23 PM
 * To change this template use File | Settings | File Templates.
 */

import org.codehaus.yagll.access.*
import org.codehaus.yagll.context.*
import javax.naming.directory.SearchControls

public class Yagll {

  static config
  static cache = new DummyCache()
  static authenticator = new SimpleAuthenticator()

  static void bind(object, HashMap entityToNameHashtable) {
    Bind.bind(object, entityToNameHashtable)
  }

  static lookup(object, HashMap entityToNameHashtable) {
    Lookup.lookup(object, entityToNameHashtable)
  }

  static List search(factory, entity, searchRoot, filter = "objectclass=*", scope = SearchControls.SUBTREE_SCOPE) {
    Search.search(factory, entity, searchRoot, filter, scope)
  }

  static void unbind(name) {
    Unbind.unbind(name)
  }

  static getPrincipal() {
    config?.toProperties()?.getProperty("principal")
  }

  static getKeytab() {
    config?.toProperties()?.getProperty("keytab")
  }

  static getUrl() {
    config?.toProperties()?.getProperty("url")
  }

  static getKrb5() {
    config?.toProperties()?.getProperty("krb5")
  }

  static getDn() {
    config?.toProperties()?.getProperty("dn")
  }

  static getPassword() {
    config?.toProperties()?.getProperty("password")
  }

  static getDebug() {
    def result = config?.toProperties()?.getProperty("debug")
    result?:"false"
  }
  
}
