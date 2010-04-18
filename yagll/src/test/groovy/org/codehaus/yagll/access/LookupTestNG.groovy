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
 * Date: Mar 7, 2009
 * Time: 4:17:42 AM
 * To change this template use File | Settings | File Templates.
 */

public class LookupTestNG {

  @BeforeClass
  void configure() {
    Yagll.config = new ConfigObject()
    Yagll.config.setProperty('url', TestConfiguration.url)
    Yagll.config.setProperty('dn', TestConfiguration.dn)
    Yagll.config.setProperty('password', TestConfiguration.password)
  }

  @Test
  void shouldSucceed() {
    def admin = Yagll.lookup(new Administrator(), ["securityObject": "cn=admin"])
    assert TestConfiguration.dn.startsWith("cn=$admin.commonName")
  }

}
