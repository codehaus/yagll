package org.codehaus.yagll.access

import org.codehaus.yagll.annotation.DirectoryBound
import org.codehaus.yagll.annotation.DirectoryEntity
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Apr 3, 2010
 * Time: 1:49:10 AM
 * To change this template use File | Settings | File Templates.
 */

@DirectoryBound (entities = ["securityObject"], objectClasses = ["simpleSecurityObject, organizationalRole"])
class Administrator {
  @DirectoryEntity (entity = "securityObject", attribute = "cn")
  String commonName

  @DirectoryEntity (entity = "securityObject", attribute = "description")
  String desc
}
