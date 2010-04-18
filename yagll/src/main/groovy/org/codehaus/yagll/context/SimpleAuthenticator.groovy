package org.codehaus.yagll.context

import org.codehaus.yagll.Yagll
import javax.naming.directory.InitialDirContext
import javax.naming.Context

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 31, 2009
 * Time: 1:30:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class SimpleAuthenticator {

  String factory = "com.sun.jndi.ldap.LdapCtxFactory"
  String url
  String dn
  String password

  InitialDirContext getInitialDirContext() {

    if (!factory) {
      throw new Exception("factory must not be blank")
    }
    Class.forName(factory)

    if (!Yagll.url.isEmpty()) {
      url = Yagll.url
    }
    if (!url) {
      throw new Exception("url must not be blank")
    } else if (!url.startsWith("ldap://")) {
      throw new Exception("url has wrong protocol")
    }

    if (!Yagll.dn.isEmpty()) {
      dn = Yagll.dn
    }
    if (!dn) {
      throw new Exception("user distinguished name must not be blank")
    }

    if (!Yagll.password.isEmpty()) {
      password = Yagll.password
    }

    Hashtable environment = new Hashtable()
    environment.put(Context.INITIAL_CONTEXT_FACTORY, factory)
    environment.put(Context.PROVIDER_URL, url)
    environment.put(Context.SECURITY_PRINCIPAL, dn)
    if (password) {
      environment.put(Context.SECURITY_CREDENTIALS, password)
    }

    new InitialDirContext(environment)

  }

}