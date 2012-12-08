package org.codehaus.yagll.authentication;

import org.codehaus.yagll.UnsetVariableException;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Sep 13, 2010
 * Time: 10:53:58 PM
 * To change this template use File | Settings | File Templates.
 */

public class LdapConfiguration extends Configuration {

    public static final String KERBEROS = "com.sun.security.auth.module.Krb5LoginModule";

    private String principal;
    private String keytab;
    private String debug;

    Hashtable<String, String> options = new Hashtable<String, String>();

    public LdapConfiguration(String principal, String keytab, String debug) throws UnsetVariableException {
        if (principal == null) {
            throw new UnsetVariableException("principal must be set");
        }
        if (keytab == null) {
            throw new UnsetVariableException("keytab must be set");
        }
        if (debug == null || !(debug.equals("true") || debug.equals("false"))) {
            throw new UnsetVariableException("debug must be set to 'true' or 'false'");
        }
        this.principal = principal;
        this.keytab = keytab;
        this.debug = debug;
        Configuration.setConfiguration(this);
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getKeytab() {
        return keytab;
    }

    public void setKeytab(String keytab) {
        this.keytab = keytab;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        options.put("principal", principal);
        options.put("keyTab", keytab);
        options.put("debug", debug);
        options.put("useKeyTab", "true");

        AppConfigurationEntry[] appConfigurationEntries = new AppConfigurationEntry[1];
        appConfigurationEntries[0] = new AppConfigurationEntry(KERBEROS, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);

        return appConfigurationEntries;
    }

}

