package org.codehaus.yagll.access

import org.codehaus.yagll.Yagll
import javax.naming.directory.SearchControls

Yagll.config = new ConfigObject()
Yagll.config.setProperty('url', "ldap://172.19.191.154/dc=devicesoft,dc=org")
Yagll.config.setProperty('dn', "cn=admin,dc=devicesoft,dc=org")
Yagll.config.setProperty('password', "secret")

def foundingFathers = [
  new FoundingFather(name: 'George Washington',
          lastName: 'Washington', modernUsername: 'george'),
  new FoundingFather(name: 'Benjamin Franklin',
          lastName: 'Franklin', modernUsername: 'benjamin'),
  new FoundingFather(name: 'Thomas Jefferson',
          lastName: 'Jefferson', modernUsername: 'thomas'),
]

foundingFathers.each { Yagll.bind(it,
        [names: "uid=$it.modernUsername,cn=people" as String]
)}

def franklin = foundingFathers.find { it.name == 'Benjamin Franklin' }
franklin.name = 'Ben Franklin'
Yagll.bind(franklin, [names: "uid=benjamin,cn=people"])

franklin = Yagll.lookup(new FoundingFather(), [names: "uid=benjamin,cn=people"])
franklin.name = 'Benjamin Franklin'
Yagll.bind(franklin, [names: "uid=benjamin,cn=people"])
