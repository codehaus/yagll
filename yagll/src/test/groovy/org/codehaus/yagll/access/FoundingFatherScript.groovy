package org.codehaus.yagll.access

import org.codehaus.yagll.Yagll
import javax.naming.directory.SearchControls

Yagll.config = new ConfigObject()
Yagll.config.setProperty('url', "ldap://172.19.191.154/dc=devicesoft,dc=org")
Yagll.config.setProperty('dn', "cn=admin,dc=devicesoft,dc=org")
Yagll.config.setProperty('password', "secret")

def foundingFathers = Yagll.search(new FoundingFather(), "names", "cn=people",
        "objectClass=inetOrgPerson", SearchControls.ONELEVEL_SCOPE)

foundingFathers.each { println it.name } 
