package org.codehaus.yagll.access

import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.codehaus.yagll.Yagll
import org.codehaus.yagll.context.*
import org.codehaus.yagll.annotation.*
import javax.naming.directory.BasicAttribute
import javax.naming.directory.BasicAttributes
import javax.naming.directory.SearchControls
import javax.naming.ldap.LdapName

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 19, 2009
 * Time: 4:22:31 AM
 * To change this template use File | Settings | File Templates.
 */

public class SearchTestNG {
  @BeforeClass
  void configure() {
    Yagll.config = new ConfigObject()
    Yagll.config.setProperty('url', TestConfiguration.url)
    Yagll.config.setProperty('dn', TestConfiguration.dn)
    Yagll.config.setProperty('password', TestConfiguration.password)
  }

  @Test
  void shouldSucceed() {
    assert Yagll.search(new Administrator(), "securityObject", "", "cn=admin", SearchControls.ONELEVEL_SCOPE)
  }
}
