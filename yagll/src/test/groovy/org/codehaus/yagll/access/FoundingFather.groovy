package org.codehaus.yagll.access

import org.codehaus.yagll.annotation.DirectoryBound
import org.codehaus.yagll.annotation.DirectoryEntity

@DirectoryBound (entities = ["names"], objectClasses = ["top, person, organizationalPerson, inetOrgPerson"])
class FoundingFather {

  @DirectoryEntity (entity = "names", attribute = "cn")
  String name

  @DirectoryEntity (entity = "names", attribute = "sn")
  String lastName

  @DirectoryEntity (entity = "names", attribute = "uid")
  String modernUsername

}
