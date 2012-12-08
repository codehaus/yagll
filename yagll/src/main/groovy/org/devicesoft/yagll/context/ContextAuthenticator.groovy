package org.devicesoft.yagll.context

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Dec 4, 2010
 * Time: 7:55:37 AM
 * To change this template use File | Settings | File Templates.
 */

import javax.naming.directory.InitialDirContext
import javax.naming.Context
import javax.naming.InitialContext
import org.codehaus.yagll.factory.UsableInitialDirContextFactory

class ContextAuthenticator {

  InitialDirContext getInitialDirContext() {

    Context initialContext = new InitialContext()
    UsableInitialDirContextFactory factory = initialContext.lookup("java:comp/env/jndi/devicesoft")  as UsableInitialDirContextFactory
    def ctx = factory.getInitialContext(null)
      ctx.addToEnvironment("java.naming.ldap.attributes.binary", "userPKCS12 userPassword");
    ctx
  }

}

