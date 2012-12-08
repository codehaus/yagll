package org.devicesoft.yagll.access

import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.devicesoft.yagll.annotation.*
import org.devicesoft.yagll.context.*
import org.devicesoft.yagll.Yagll
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

  def configObject
  def config

  @BeforeClass
  void configureAndPopulateIntegration() {
      configObject = new ConfigSlurper().parse(new URL("file:///root/builder/grails-app/conf/Config.groovy"))["simple"]
      Yagll.config = configObject
      config = configObject.selftest.toProperties()

    def ctx = LdapUtils.getInitialDirContext()
    def ocAttr = new BasicAttribute("objectclass")
    ocAttr.add("top")
    ocAttr.add("person")
    ocAttr.add("organizationalPerson")
    ocAttr.add("inetOrgPerson")
    def attrs = new BasicAttributes()
    attrs.put(ocAttr)
    attrs.put("uid", config.getProperty("uid"))
    attrs.put("cn", config.getProperty("cn"))
    attrs.put("sn", config.getProperty("sn"))

    try {
      ctx.bind("uid=${config.getProperty("uid")}", null, attrs)
    } catch (e) {}

    ocAttr = new BasicAttribute("objectclass")
    ocAttr.add("top")
    ocAttr.add("country")
    attrs = new BasicAttributes()
    attrs.put(ocAttr)
    attrs.put("c", "ZZ")

    try {
      ctx.bind("c=ZZ", null, attrs)
    } catch (e) {}
  }

  @Test
  void shouldSucceedIfEntityNameDoesNotExist() {
    def retrievableInstance = new BoundToTestAccountType()
    Yagll.lookup(retrievableInstance, ["This Entity Name Does Not Exist": "uid=${config.getProperty("uid")}"])
    assert !retrievableInstance.commonName
    assert !retrievableInstance.userName
  }

  @Test
  void shouldSucceedIfEntryDoesNotExist() {
    def retrievableInstance = new BoundToTestAccountType()
    Yagll.lookup(retrievableInstance, ["Test Account": "uid=this entry does not exist"])
    assert !retrievableInstance.commonName
    assert !retrievableInstance.userName
  }

  @Test
  void shouldSucceedIfObjectIsNotAnnotated() {
    def retrievableInstance = new Expando()
    Yagll.lookup(retrievableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])
    assert !retrievableInstance.commonName
    assert !retrievableInstance.userName
  }

  @Test
  void shouldSucceed() {
    def retrievableInstance = new BoundToTestAccountType()
    Yagll.lookup(retrievableInstance, ["Test Account": "uid=${config.getProperty("uid")}", "Location": "c=ZZ"])
    def name = "uid=${config.getProperty("uid")},$LdapUtils.initialDirContext.nameInNamespace"
    def nameInContext = "uid=${config.getProperty("uid")}"
    assert !(retrievableInstance.commonName - config.getProperty("cn"))
    assert retrievableInstance.userName == config.getProperty("uid")
    assert retrievableInstance.country == "ZZ"
    assert new LdapName(name).compareTo(new LdapName(retrievableInstance.distinguishedName)) == 0
    assert new LdapName(nameInContext).compareTo(new LdapName(retrievableInstance.distinguishedNameInContext)) == 0
  }

}
