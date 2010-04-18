package org.codehaus.yagll.context

import javax.security.auth.login.LoginContext
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.Callback

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Feb 27, 2009
 * Time: 4:20:49 AM
 * To change this template use File | Settings | File Templates.
 */

public class Krb5Login implements CallbackHandler {

  LoginContext login(principal, keytab) {
    def configuration = new LdapConfiguration(principal, keytab)
    def loginContext = new LoginContext("unused", null, this, configuration)
    loginContext.login()
    loginContext
  }

  void handle(Callback[] callbacks) {}

}