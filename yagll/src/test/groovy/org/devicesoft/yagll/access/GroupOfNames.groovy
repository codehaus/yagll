package org.devicesoft.yagll.access
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 27, 2009
 * Time: 8:01:36 PM
 * To change this template use File | Settings | File Templates.
 */

import org.devicesoft.yagll.annotation.*

@DirectoryBound( entities=["groupOfNames"], objectClasses=["top, groupOfNames"])
public class GroupOfNames {

  @DirectoryEntity(entity="groupOfNames", attribute="cn")
  String commonName

  @DirectoryEntity(entity="groupOfNames", attribute="member", isByteArray = true, mustBeUnique = true)
  Set<String> member

  def fieldFactory(fieldName) {
    switch (fieldName) {
      case "member":
        return [] as Set
      default:
        return ""
    }
  }

}
