package org.devicesoft.yagll.access

import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.devicesoft.yagll.Yagll
import org.devicesoft.yagll.context.*
import org.devicesoft.yagll.annotation.*
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

  def configObject
  def config

  @BeforeClass
  void configureAndPopulate() {
    configObject = new ConfigSlurper().parse(new URL("${System.getProperty("configuration.url")}/yagll.groovy"))
    Yagll.config = configObject.simple
    config = configObject.selftest.search.toProperties()

    String searchRoot = config.getProperty("searchRoot")
    String searchRootCN = config.getProperty("searchRootCN")

    def ctx = LdapUtils.getInitialDirContext()
    def ocAttr = new BasicAttribute("objectclass")
    ocAttr.add("top")
    ocAttr.add("organizationalRole")
    def attrs = new BasicAttributes()
    attrs.put(ocAttr)
    attrs.put("cn", searchRootCN)

    try {
      ctx.bind(searchRoot, null, attrs)
    } catch (e) {}

    String uid = config.getProperty("uid")
    String cn = config.getProperty("cn")
    String sn = config.getProperty("sn")

    (0..9).collect { it.toString() }.each {
      ocAttr = new BasicAttribute("objectclass")
      ocAttr.add("top")
      ocAttr.add("person")
      ocAttr.add("organizationalPerson")
      ocAttr.add("inetOrgPerson")
      attrs = new BasicAttributes()
      attrs.put(ocAttr)
      attrs.put("uid", uid + it)
      attrs.put("cn", cn + it)
      attrs.put("sn", sn + it)

      try {
        ctx.bind("uid=$uid$it,$searchRoot", null, attrs)
      } catch (e) {}
    }
  }

  @Test
  void shouldFailIfClassIsNotBound() {
    def entity = "Test Account"
    String searchRoot = config.getProperty("searchRoot")
    def filter = "objectclass=inetOrgPerson"
    def results = Search.search(new Expando(), entity, searchRoot, filter)
    assert results.size() == 0
  }

  @Test
  void shouldSucceedIfEntityNameDoesNotExist() {
    def entity = "This Entity Does Not Exist"
    String searchRoot = config.getProperty("searchRoot")
    def filter = "objectclass=inetOrgPerson"
    def results = Search.search(new BoundToTestAccountType(), entity, searchRoot, filter)
    results.each { println it.properties }
    assert results.size() == 0
  }

  @Test
  void shouldSucceedWhenFilterFindsNothing() {
    def entity = "Test Account"
    String searchRoot = config.getProperty("searchRoot")
    def filter = "objectclass=thisDoesNotExist"
    def results = Search.search(new BoundToTestAccountType(), entity, searchRoot, filter)
    assert results.size() == 0
  }

    @Test
    void shouldSucceedWithDefaultFilter() {
      def entity = "Test Account"
      String searchRoot = config.getProperty("searchRoot")
      def results = Search.search(new BoundToTestAccountType(), entity, searchRoot)
      assert results.size() == 11
      results.each { println it.properties }

      String commonName = config.getProperty("cn")
      String lastName = config.getProperty("sn")
      String userName = config.getProperty("uid")
      def found

      (0..9).collect { it.toString() }.each {idx ->
        found = results.find { !(it.commonName - (commonName + idx)) }
        assert found
        found = results.find { it.lastName == lastName + idx }
        assert found
        found = results.find { it.userName == userName + idx }
        assert found
      }
      commonName = config.getProperty("searchRootCN")
      found = results.find { !(it.commonName - commonName) }
      assert found
  }

  @Test
  void shouldSucceedWithDifferentScope() {
    def entity = "Test Account"
    String searchRoot = config.getProperty("searchRoot")
    def filter = "objectclass=*"
    def results = Search.search(new BoundToTestAccountType(), entity, searchRoot, filter, SearchControls.ONELEVEL_SCOPE)
    assert results.size() == 10

    String commonName = config.getProperty("cn")
    String lastName = config.getProperty("sn")
    String userName = config.getProperty("uid")
    def found

    (0..9).collect { it.toString() }.each {idx ->
      found = results.find { !(it.commonName - (commonName + idx)) }
      assert found
      found = results.find { it.lastName == lastName + idx }
      assert found
      found = results.find { it.userName == userName + idx }
      assert found
    }
  }

  @Test
  void shouldSucceed() {
    def entity = "Test Account"
    String searchRoot = config.getProperty("searchRoot")
    def filter = "objectclass=inetOrgPerson"
    def results = Search.search(new BoundToTestAccountType(), entity, searchRoot, filter)

    assert results.size() == 10

    String commonName = config.getProperty("cn")
    String lastName = config.getProperty("sn")
    String userName = config.getProperty("uid")
    def found

    (0..9).collect { it.toString() }.each {idx ->
      found = results.find { !(it.commonName - (commonName + idx)) }
      assert found
      found = results.find { it.lastName == lastName + idx }
      assert found
      found = results.find { it.userName == userName + idx }
      assert found

      def name = "uid=$userName$idx,cn=test people,$LdapUtils.initialDirContext.nameInNamespace"
      def nameInContext = "uid=$userName$idx,cn=test people"

      found = results.find { new LdapName(name).compareTo(new LdapName(it.distinguishedName)) == 0 }
      assert found
      found = results.find { new LdapName(nameInContext).compareTo(new LdapName(it.distinguishedNameInContext)) == 0 }
      assert found
    }
  }

}
