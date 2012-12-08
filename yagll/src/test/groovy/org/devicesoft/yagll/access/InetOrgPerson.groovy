package org.devicesoft.yagll.access

import org.devicesoft.yagll.Yagll
import org.devicesoft.yagll.annotation.DirectoryBound
import org.devicesoft.yagll.annotation.DirectoryEntity

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Jan 21, 2010
 * Time: 7:48:16 AM
 * To change this template use File | Settings | File Templates.
 */

@DirectoryBound (entities = ["person"], objectClasses = ["top, person, organizationalPerson, inetOrgPerson"])
class InetOrgPerson {

    @DirectoryEntity (entity = "person", attribute = "cn")
    String cn

    @DirectoryEntity (entity = "person", attribute = "sn")
    String sn

    @DirectoryEntity (entity = "person", attribute = "userPKCS12", isByteArray = true)
    byte[] userPKCS12

    def fieldFactory(field) {
        if (field == "userPKCS12") {
            return new byte[0]
        }
        return ""

    }

}

