package org.codehaus.yagll.access

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Apr 3, 2010
 * Time: 1:59:11 AM
 * To change this template use File | Settings | File Templates.
 */
class TestConfiguration {
  public static final String url = "ldap://172.19.191.154/dc=devicesoft,dc=org"
  public static final String dn = "cn=admin,dc=devicesoft,dc=org"
  public static final String password = "secret"

  public static final String principal = "root/devicesoft.org@DEVICESOFT.ORG"
  public static final String keytab = "C:/Users/brett/Documents/krb5.keytab"
  public static final String krb5url = "ldap://management.devicesoft.org/dc=devicesoft,dc=org"
  public static final String krb5 = "C:/Users/brett/Documents/krb5.conf"
  public static final String debug = "false"
}
