package org.codehaus.yagll.annotation
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 20, 2009
 * Time: 4:22:25 PM
 * To change this template use File | Settings | File Templates.
 */

@DirectoryBound( entities=["Test Account"],
objectClasses=["top, person, organizationalPerson, inetOrgPerson"])
public class BoundToSingleAccountType {

  @DirectoryEntity(entity="Test Account", attribute="cn")
  String commonName

  @DirectoryEntity(entity="Test Account", attribute="sn")
  String lastName

  @DirectoryEntity(entity="Test Account", attribute="uid")
  String userName

}
