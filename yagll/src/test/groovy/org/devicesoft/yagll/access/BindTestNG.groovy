package org.devicesoft.yagll.access

import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.devicesoft.yagll.Yagll
import org.devicesoft.yagll.context.*
import org.devicesoft.yagll.annotation.*
import javax.naming.directory.InvalidAttributeValueException
import javax.naming.directory.SchemaViolationException
import org.testng.annotations.BeforeTest
import org.testng.annotations.AfterTest

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 4:26:51 AM
 * To change this template use File | Settings | File Templates.
 */

public class BindTestNG {

  def configObject
  def config

  @BeforeClass
  void unbind() {
    configObject = new ConfigSlurper().parse(new URL("${System.getProperty('configuration.url')}/yagll.groovy"))
    Yagll.config = configObject.simple
    config = configObject.selftest.toProperties()

    def ctx = LdapUtils.getInitialDirContext()

    try {
      ctx.unbind("uid=${config.getProperty("uid")}")
      ctx.unbind("cn=${config.getProperty("group.cn")}")
    } catch (e) { println e.message }
  }

  @Test (expectedExceptions = [InvalidAttributeValueException, SchemaViolationException])
  void shouldThrowIfCnIsBlank() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.lastName = config.getProperty("sn")
    saveableInstance.userName = config.getProperty("uid")
    Yagll.bind(saveableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])
  }

  @Test (expectedExceptions = [InvalidAttributeValueException, SchemaViolationException])
  void shouldThrowIfSnIsBlank() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.commonName = [config.getProperty("cn")] as Set
    saveableInstance.userName = config.getProperty("uid")
    Yagll.bind(saveableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])
  }

  @Test
  void shouldSucceedIfUidIsBlank() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.commonName = [config.getProperty("cn")] as Set
    saveableInstance.lastName = config.getProperty("sn")
    Yagll.bind(saveableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])
    unbind()
  }

  @Test
  void shouldSucceedIfNotAnnotated() {
    def saveableInstance = new Expando()
    saveableInstance.commonName = [config.getProperty("cn")] as Set
    saveableInstance.lastName = config.getProperty("sn")
    saveableInstance.userName = config.getProperty("uid")
    Yagll.bind(saveableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])
  }

  @Test
  void shouldSucceedIfEntityNameDoesNotExist() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.commonName = [config.getProperty("cn")] as Set
    saveableInstance.lastName = config.getProperty("sn")
    saveableInstance.userName = config.getProperty("uid")
    Yagll.bind(saveableInstance, ["This Entity Name Does Not Exist": "uid=${config.getProperty("uid")}"])
  }

  @Test
  void shouldSucceedIfEntityDoesNotExist() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.commonName = [config.getProperty("cn")] as Set
    saveableInstance.lastName = config.getProperty("sn")
    saveableInstance.userName = config.getProperty("uid")
    Yagll.bind(saveableInstance, ["This Entity Name Does Not Exist": "uid=this does not exist"])
  }

  @Test
  void shouldSucceed() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.commonName = [config.getProperty("cn")] as Set
    saveableInstance.lastName = config.getProperty("sn")
    saveableInstance.userName = config.getProperty("uid")
    saveableInstance.distinguishedNameInContext = "uid=$saveableInstance.userName"
    saveableInstance.distinguishedName = "uid=$saveableInstance.userName"
    Yagll.bind(saveableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])

    def existing = new BoundToTestAccountType()
    if (!Yagll.lookup(existing, ["Test Account": "uid=${config.getProperty("uid")}"])) {
      throw new Exception("data doesn't exist yet")
    }
    existing.lastName = config.getProperty("sn") + "ModifyLastName"
    Yagll.bind(existing, ["Test Account": "uid=${config.getProperty("uid")}"])
    unbind()
  }

  @Test
  void shouldSucceedBindingDistinguishedNames() {
    def groupOfNames = new GroupOfNames()
    groupOfNames.commonName = config.getProperty("group.cn")
    groupOfNames.member = [config.getProperty("group.member1")] as Set
    Yagll.bind(groupOfNames, ["groupOfNames": "cn=${config.getProperty("group.cn")}"])
    groupOfNames.member.add(config.getProperty("group.member2"))
    Yagll.bind(groupOfNames, ["groupOfNames": "cn=${config.getProperty("group.cn")}"])
    Yagll.unbind("cn=${config.getProperty("group.cn")}")
  }

}