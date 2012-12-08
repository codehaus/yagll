package org.devicesoft.yagll.access

import org.devicesoft.yagll.Yagll
import org.devicesoft.yagll.annotation.BoundToTestAccountType
import org.devicesoft.yagll.context.LdapUtils
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import javax.naming.directory.InvalidAttributeValueException
import javax.naming.directory.SchemaViolationException

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 4:26:51 AM
 * To change this template use File | Settings | File Templates.
 */

public class BinaryTestNG {

    def configObject
    def config
    def pkcs12
    def anotherPkcs12
    def inetOrgPerson

    @BeforeClass
    void initialize() {
        configObject = new ConfigSlurper().parse(new URL("file:///root/builder/grails-app/conf/Config.groovy"))["simple"]
        Yagll.config = configObject
        config = configObject.selftest.toProperties()

        def ctx = LdapUtils.getInitialDirContext()

        def entity = "person"
        String searchRoot = config.getProperty("searchRoot")
        def filter = "(&(objectclass=inetOrgPerson)(${config.getProperty('searchRoot')}))"
        def results = Search.search(new InetOrgPerson(), entity, searchRoot, filter)
        println "results.size(): " + results.size()
        assert(results.size() == 1);
        inetOrgPerson = results[0];

        pkcs12 = new File(config.getProperty("pkcs12")).readBytes()
        anotherPkcs12 = new File(config.getProperty("anotherPkcs12")).readBytes()
    }
/*
  @Test (expectedExceptions = [InvalidAttributeValueException, SchemaViolationException])
  void shouldThrowIfCnIsBlank() {
    def saveableInstance = new BoundToTestAccountType()
    saveableInstance.lastName = config.getProperty("sn")
    saveableInstance.userName = config.getProperty("uid")
    Yagll.bind(saveableInstance, ["Test Account": "uid=${config.getProperty("uid")}"])
  }
  */

  @Test
  void shouldSucceed() {
      def inetOrgPerson = Yagll.lookup(new InetOrgPerson(), ["person": config.getProperty('searchRoot')]);
      assert inetOrgPerson
      assert inetOrgPerson.userPKCS12.length == pkcs12.length
      for (int i = 0; i < pkcs12.length; i++) {
          assert inetOrgPerson.userPKCS12[i] == pkcs12[i]
      }
      inetOrgPerson.userPKCS12 = anotherPkcs12
      Yagll.bind(inetOrgPerson, ["person": config.getProperty('searchRoot')]);
      inetOrgPerson = Yagll.lookup(new InetOrgPerson(), ["person": config.getProperty('searchRoot')]);
      assert inetOrgPerson
      assert inetOrgPerson.userPKCS12.length == anotherPkcs12.length
      inetOrgPerson.userPKCS12 = pkcs12
      Yagll.bind(inetOrgPerson, ["person": config.getProperty('searchRoot')]);
      inetOrgPerson = Yagll.lookup(new InetOrgPerson(), ["person": config.getProperty('searchRoot')]);
      assert inetOrgPerson
      assert inetOrgPerson.userPKCS12.length == pkcs12.length
  }
}
