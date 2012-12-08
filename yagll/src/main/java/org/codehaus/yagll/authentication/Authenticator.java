package org.codehaus.yagll.authentication;

import javax.naming.directory.DirContext;

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Sep 13, 2010
 * Time: 10:36:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Authenticator {

    public DirContext getInitialDirContext();
    
}
