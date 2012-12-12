package org.codehaus.yagll.access

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Apr 3, 2010
 * Time: 1:59:11 AM
 * To change this template use File | Settings | File Templates.
 */
class TestConfiguration {
  public static final String url = "ldap://east-gateway.devicesoft.org/dc=devicesoft,dc=org"
  public static final String dn = "cn=admin,dc=devicesoft,dc=org"
  public static final String password = "Flawed01"

  public static final String principal = "root/devicesoft.org@DEVICESOFT.ORG"
  public static final String keytab = "C:/Users/brett/Desktop/root.keytab"
  public static final String krb5url = "ldap://east-gateway.devicesoft.org/dc=devicesoft,dc=org"
  public static final String krb5 = "C:/Windows/krb5.conf"
  public static final String debug = "false"
}
