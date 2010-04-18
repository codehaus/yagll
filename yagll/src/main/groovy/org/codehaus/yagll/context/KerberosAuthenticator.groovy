package org.codehaus.yagll.context

import javax.naming.directory.InitialDirContext
import org.codehaus.yagll.Yagll
import javax.naming.AuthenticationException
import javax.naming.directory.SearchControls
import javax.security.auth.login.LoginContext

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 23, 2009
 * Time: 10:34:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class KerberosAuthenticator {

  static InitialDirContext context = null
  static LoginContext loginContext = null

  InitialDirContext getInitialDirContext() {

    def saslAuthenticator

    if (!context) {
      loginContext?.logout()
      loginContext = new Krb5Login().login(Yagll.principal, Yagll.keytab)
      saslAuthenticator = new SaslAuthentication(Yagll.url, Yagll.krb5, loginContext.subject)
      context = saslAuthenticator.getInitialDirContext()
      return context
    }
    try {
      context.search("cn=this is used to test the connection", "objectClass=*", new SearchControls(searchScope: SearchControls.OBJECT_SCOPE))
      return context
    } catch (javax.naming.NameNotFoundException nnfe) {
      return context
    } catch (e) {
      e.printStackTrace()
    }
    System.err.println "KerberosAuthenticator: getting new context..."

    loginContext?.logout()
    loginContext = new Krb5Login().login(Yagll.principal, Yagll.keytab)
    saslAuthenticator = new SaslAuthentication(Yagll.url, Yagll.krb5, loginContext.subject)
    context?.close()
    context = saslAuthenticator.getInitialDirContext()
    context

  }

}