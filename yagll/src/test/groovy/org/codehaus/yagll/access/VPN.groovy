package org.codehaus.yagll.access

import org.codehaus.yagll.annotation.*
import org.codehaus.yagll.Yagll

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Jan 21, 2010
 * Time: 7:48:16 AM
 * To change this template use File | Settings | File Templates.
 */

@DirectoryBound (entities = ["vpn", "container"], objectClasses = ["top, monadVpn", "top, groupOfUniqueNames"])
class VPN {
  public static final String VPN_BASE = "ou=vpn"
  public static final String SERVERS = "cn=servers,$VPN_BASE"
  public static final String CLIENTS = "cn=clients,$VPN_BASE"

  @DirectoryEntity (entity = "vpn", attribute = "monadcn")
  String cn

  @DirectoryEntity (entity = "vpn", attribute = "monadEndpoint")
  String endpoint

  @DirectoryEntity (entity = "vpn", attribute = "monadDeviceName")
  String host

  @DirectoryEntity (entity = "vpn", attribute = "monadIpAddress")
  String ipAddress

  @DirectoryEntity (entity = "vpn", attribute = "monadSubnet")
  Set<String> subnet

  @DirectoryEntity (entity = "vpn", attribute = "monadSubnetWithin")
  Set<String> subnetWithin

  @DirectoryEntity (entity = "vpn", attribute = "monadHiddenSubnet")
  Set<String> hiddenSubnet

  @DirectoryEntity (entity = "vpn", attribute = "monadHiddenSubnetWithin")
  Set<String> hiddenSubnetWithin

  @DirectoryEntity (entity = "vpn", attribute = "=")
  String distinguishedName

  @DirectoryEntity (entity = "container", attribute = "cn")
  String category

  @DirectoryEntity (entity = "container", attribute = "uniqueMember", mustBeUnique = true)
  Set<String> devices

  def fieldFactory(field) {
    if (field == 'subnet' || field == 'subnetWithin' || field == 'devices' || field == 'hiddenSubnet' || field == 'hiddenSubnetWithin') {
      return [] as Set
    }
    return ""
  }

  def save() {
    if (category == "IPsec Gateways") {
      def existing = Yagll.lookup(new VPN(), [container: GATEWAYS])
    }

  }
}

