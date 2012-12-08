package org.codehaus.yagll.factory;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 12/13/10
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.naming.*;
import javax.naming.directory.InitialDirContext;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;

import org.codehaus.yagll.authentication.Authenticator;
import org.codehaus.yagll.authentication.KerberosAuthenticator;
import org.codehaus.yagll.authentication.SimpleAuthenticator;

import java.util.Enumeration;
import java.util.Hashtable;

public class UsableInitialDirContextFactory implements ObjectFactory, InitialContextFactory {

    /*
    *
    * Reference should have these elements
    *
    *
   String principal = "root/devicesoft.org@DEVICESOFT.ORG"
   String keytab = "C:/Users/brett/Documents/krb5.keytab"
   String krb5url = "ldap://management.devicesoft.org/dc=devicesoft,dc=org"
   String krb5 = "C:/Users/brett/Documents/krb5.conf"
   String debug = "false"
    */

    Hashtable<String, String> params = new Hashtable<String, String>();

    public UsableInitialDirContextFactory() {

    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws NamingException {
        Reference ref = (Reference) obj;
        Enumeration addrs = ref.getAll();

        while (addrs.hasMoreElements()) {
            RefAddr addr = (RefAddr) addrs.nextElement();
            String key = addr.getType();
            String value = addr.getContent().toString();
            params.put(key, value);
        }
        return this;
    }

    public InitialDirContext getInitialContext(Hashtable<?, ?> unused) throws NamingException {

        if (params.get("binddn") != null) {
            SimpleAuthenticator authenticator = new SimpleAuthenticator();

            authenticator.setUrl((String) params.get("url"));
            authenticator.setBinddn((String) params.get("binddn"));
            authenticator.setPassword((String) params.get("password"));

            return authenticator.getInitialDirContext();
        } else {
            KerberosAuthenticator authenticator = new KerberosAuthenticator();

            authenticator.setDebug((String) params.get("debug"));
            authenticator.setKeytab((String) params.get("keytab"));
            authenticator.setKrb5Conf((String) params.get("krb5Conf"));
            authenticator.setPrincipal((String) params.get("principal"));
            authenticator.setUrl((String) params.get("url"));

            return authenticator.getInitialDirContext();
        }
    }
}

