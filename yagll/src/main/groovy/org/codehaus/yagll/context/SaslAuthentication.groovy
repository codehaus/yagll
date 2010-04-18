package org.codehaus.yagll.context

import javax.naming.directory.InitialDirContext
import java.security.PrivilegedAction
import javax.security.auth.Subject

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Feb 27, 2009
 * Time: 4:38:54 AM
 * To change this template use File | Settings | File Templates.
 */

public class SaslAuthentication implements PrivilegedAction<InitialDirContext> {

  def url
  def krb5
  def subject

  SaslAuthentication(url, krb5, subject) {
    this.url = url
    this.krb5 = krb5
    this.subject = subject
  }

  InitialDirContext run() {
    Hashtable environment = new Hashtable()

    environment.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory")
    environment.put("java.naming.security.authentication", "GSSAPI")
    environment.put("javax.security.sasl.qop", "auth-conf")
    environment.put("java.naming.provider.url", url)
    environment.put("java.security.krb5.conf", krb5)

    new InitialDirContext(environment)
  }

  InitialDirContext getInitialDirContext() {
    Subject.doAs(subject, this)
  }
}