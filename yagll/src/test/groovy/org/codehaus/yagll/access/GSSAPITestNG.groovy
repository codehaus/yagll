package org.codehaus.yagll.access

import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.codehaus.yagll.annotation.*
import org.codehaus.yagll.context.*
import org.codehaus.yagll.Yagll
import javax.naming.directory.BasicAttribute
import javax.naming.directory.BasicAttributes
import javax.naming.ldap.LdapName

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Apr 3, 2010
 * Time: 9:09:37 AM
 * To change this template use File | Settings | File Templates.
 */
class GSSAPITestNG {

  @BeforeClass
  void configure() {
    Yagll.config = new ConfigObject()
    Yagll.config.setProperty('principal', TestConfiguration.principal)
    Yagll.config.setProperty('keytab', TestConfiguration.keytab)
    Yagll.config.setProperty('url', TestConfiguration.krb5url)
    Yagll.config.setProperty('krb5', TestConfiguration.krb5)
    Yagll.config.setProperty('debug', TestConfiguration.debug)

    Yagll.authenticator = new KerberosAuthenticator()
  }

  @Test
  void shouldSucceed() {
    def admin = Yagll.lookup(new Administrator(), ["securityObject": "cn=admin"])
    assert TestConfiguration.dn.startsWith("cn=$admin.commonName")
  }

}
