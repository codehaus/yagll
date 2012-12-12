package org.codehaus.yagll.access

import org.testng.annotations.Test
import org.testng.annotations.BeforeClass
import org.codehaus.yagll.Yagll
import org.codehaus.yagll.context.*
import org.codehaus.yagll.annotation.*
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

class BindTest2NG {
  def configObject
  def config

  @BeforeClass
  void unbind() {
    Yagll.config = new ConfigSlurper().parse(new URL("file:///root/builder/grails-app/conf/Config.groovy"))["gssapi"]
    Yagll.cache = new DummyCache()
    Yagll.authenticator = new KerberosAuthenticator()
  }

  @Test
  void shouldSucceed() {
    def fhostname = "south"
    def name = "monadcn=$fhostname,$VPN.VPN_BASE" as String


    def existing = Yagll.lookup(new VPN(), [vpn: name])
    def vpn = new VPN()

    if (existing) {
      vpn = existing
    }
    vpn.cn = fhostname
    vpn.endpoint = fhostname
    vpn.host = fhostname
    vpn.ipAddress = "172.16.1.152"

    def vpnSubnet = []
    def vpnSubnetWithin = []

    def containerName = VPN.SERVERS

    if (true) {
      ["172.16.0.0/12"].each { internal ->
        if (!vpnSubnet.contains(internal)) {
          vpnSubnet << internal
        }
      }
    }
    if (vpnSubnet) {
      vpn.subnet = vpnSubnet as Set
    }
    if (vpnSubnetWithin) {
      vpn.subnetWithin = vpnSubnetWithin as Set
    }
    Yagll.bind(vpn, [vpn: name])

  }
}
