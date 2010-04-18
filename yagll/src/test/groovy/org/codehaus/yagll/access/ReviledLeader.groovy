package org.codehaus.yagll.access

import org.codehaus.yagll.annotation.DirectoryBound
import org.codehaus.yagll.annotation.DirectoryEntity

@DirectoryBound (entities = ["country", "name"],
objectClasses = ["top, country",
"top, person, organizationalPerson, inetOrgPerson"])
class ReviledLeader {

  @DirectoryEntity (entity = "country", attribute = "c")
  String country

  @DirectoryEntity (entity = "name", attribute = "cn")
  String name

  @DirectoryEntity (entity = "name", attribute = "sn")
  String lastName

  Integer victims
}
