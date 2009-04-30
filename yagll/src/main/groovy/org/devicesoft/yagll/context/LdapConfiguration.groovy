package org.devicesoft.yagll.context

import javax.security.auth.login.Configuration
import javax.security.auth.login.AppConfigurationEntry
import org.devicesoft.yagll.Yagll

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Feb 27, 2009
 * Time: 4:08:16 AM
 * To change this template use File | Settings | File Templates.
 */

public class LdapConfiguration extends Configuration {

  public static final KERBEROS = "com.sun.security.auth.module.Krb5LoginModule"

  def principal
  def keytab
  def debug

  LdapConfiguration(String principal, String keytab) {
    this.principal = principal
    this.keytab = keytab
    this.debug = Yagll.debug
  }

  public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
    def options = new Hashtable()
    options.put("principal", principal)
    options.put("keyTab", keytab)
    options.put("debug", debug)
    options.put("useKeyTab", "true")

    def appConfigurationEntry = new AppConfigurationEntry(KERBEROS, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
    [appConfigurationEntry] as AppConfigurationEntry[]
  }

}