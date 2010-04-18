package org.codehaus.yagll.access

import org.codehaus.yagll.Yagll

Yagll.config = new ConfigObject()
Yagll.config.setProperty('url', "ldap://172.19.191.154/dc=devicesoft,dc=org")
Yagll.config.setProperty('dn', "cn=admin,dc=devicesoft,dc=org")
Yagll.config.setProperty('password', "secret")

def reviledLeader = new ReviledLeader()
reviledLeader.country = 'KH'
reviledLeader.name = 'Pol Pot'
reviledLeader.lastName = 'Sar'
reviledLeader.victims = 2000000

Yagll.bind(reviledLeader, [country: "c=$reviledLeader.country" as String])
Yagll.bind(reviledLeader, [name: "cn=$reviledLeader.name,cn=people" as String])

def guessWho = new ReviledLeader()
guessWho.victims = 2000000

println "before any lookup"
println "country: $guessWho.country"
println "name: $guessWho.name"
println "lastName: $guessWho.lastName"
println "victims: $guessWho.victims"
println ""

Yagll.lookup(guessWho, [country: 'c=KH'])

println "after looking up country data"
println "country: $guessWho.country"
println "name: $guessWho.name"
println "lastName: $guessWho.lastName"
println "victims: $guessWho.victims"
println ""

Yagll.lookup(guessWho, [name: 'cn=Pol Pot,cn=people'])

println "after looking up person data"
println "country: $guessWho.country"
println "name: $guessWho.name"
println "lastName: $guessWho.lastName"
println "victims: $guessWho.victims"

