package org.devicesoft.yagll.context

import javax.naming.directory.InitialDirContext
import org.devicesoft.yagll.Yagll

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 23, 2009
 * Time: 10:34:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class KerberosAuthenticator {

  InitialDirContext getInitialDirContext() {
    def loginContext = new Krb5Login().login(Yagll.principal, Yagll.keytab)
    def saslAuthenticator = new SaslAuthentication(Yagll.url, Yagll.krb5, loginContext.subject)
    saslAuthenticator.getInitialDirContext()
  }

}