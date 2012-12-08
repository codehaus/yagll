package org.codehaus.yagll.factory;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Dec 3, 2010
 * Time: 10:18:42 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.naming.*;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;

import org.codehaus.yagll.authentication.KerberosAuthenticator;

import java.util.Enumeration;
import java.util.Hashtable;

public class ContextFactory implements ObjectFactory, InitialContextFactory {

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

    public ContextFactory() {
        
    }


    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws NamingException {
        Reference ref = (Reference) obj;
        Enumeration addrs = ref.getAll();
        Hashtable<String, String> params = new Hashtable<String, String>();

        while (addrs.hasMoreElements()) {
            RefAddr addr = (RefAddr) addrs.nextElement();
            String key = addr.getType();
            String value = addr.getContent().toString();
            params.put(key, value);
        }
        return getInitialContext(params);
    }

    public Context getInitialContext(Hashtable<?, ?> params) throws NamingException {
        KerberosAuthenticator authenticator = new KerberosAuthenticator();

        authenticator.setDebug((String) params.get("debug"));
        authenticator.setKeytab((String) params.get("keytab"));
        authenticator.setKrb5Conf((String) params.get("krb5Conf"));
        authenticator.setPrincipal((String) params.get("principal"));
        authenticator.setUrl((String) params.get("url"));

        return authenticator.getInitialDirContext();
    }
}
