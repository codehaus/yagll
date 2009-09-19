package org.devicesoft.yagll.annotation
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 3:39:44 AM
 * To change this template use File | Settings | File Templates.
 */

@DirectoryBound( entities=["Test Account", "Location"],
  objectClasses=["top, person, organizationalPerson, inetOrgPerson", "top, country"])
public class BoundToTestAccountType {

  @DirectoryEntity(entity="Test Account", attribute="cn")
  Set<String> commonName

  @DirectoryEntity(entity="Test Account", attribute="sn")
  String lastName

  @DirectoryEntity(entity="Test Account", attribute="uid")
  String userName

  @DirectoryEntity(entity="Test Account", attribute="==")
  String distinguishedName

  @DirectoryEntity(entity="Test Account", attribute="=")
  String distinguishedNameInContext

  @DirectoryEntity(entity="Location",  attribute="c")
  String country

  def fieldFactory(fieldName) {
    switch (fieldName) {
      case "commonName":
        return [] as Set<String>
      default:
        return ""
    }
  }
}