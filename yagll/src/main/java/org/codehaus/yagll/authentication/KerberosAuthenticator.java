package org.codehaus.yagll.authentication;

import org.apache.log4j.Logger;
import org.codehaus.yagll.UnsetVariableException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Sep 13, 2010
 * Time: 10:40:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class KerberosAuthenticator implements Authenticator {

    private static InitialDirContext context = null;
    private static LoginContext loginContext = null;

    private String principal; // "root/devicesoft.org@DEVICESOFT.ORG"
    private String keytab; // "C:/Users/brett/Documents/krb5.keytab"
    private String url; // "ldap://management.devicesoft.org/dc=devicesoft,dc=org"
    private String krb5Conf; // "C:/Users/brett/Documents/krb5.conf"
    private String debug; // "false"

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKrb5Conf() {
        return krb5Conf;
    }

    public void setKrb5Conf(String krb5Conf) {
        this.krb5Conf = krb5Conf;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public InitialDirContext getInitialDirContext() {

        if (context != null) {
            try {
                SearchControls searchControls = new SearchControls();
                searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);

                context.search("cn=this is used to test the connection", "objectClass=*", searchControls);
            } catch (NameNotFoundException nnfe) {
                return context;
            } catch (NamingException ne) {
                Logger.getLogger(this.getClass()).error("", ne);
            }
            //
            // we have a bad context
            //
        }
        if (loginContext != null) {
            try {
                loginContext.logout();
            } catch (LoginException le) {
                Logger.getLogger(this.getClass()).error("", le);
            }
            //
            // loginContext is invalid
            //
        }
        try {
            Krb5Login krb5Login = new Krb5Login(principal, keytab, debug);

            loginContext = krb5Login.login();
            //
            // we have a login context
            //
            SaslAuthentication saslAuthentication = new SaslAuthentication(url, krb5Conf, loginContext.getSubject());

            context = saslAuthentication.getInitialDirContext();

            if (context == null) {
                Logger.getLogger(this.getClass()).error("context is null");
            } else {
                try {
                    context.addToEnvironment("java.naming.ldap.attributes.binary", "userPKCS12");
                } catch (NamingException ne) {}
            }
            return context;

        } catch (UnsetVariableException uve) {
            Logger.getLogger(this.getClass()).error("", uve);
            return null;
        } catch (LoginException le) {
            Logger.getLogger(this.getClass()).error("", le);
            return null;
        }
    }

}
