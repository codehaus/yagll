package org.codehaus.yagll.access
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 16, 2009
 * Time: 8:40:05 PM
 * To change this template use File | Settings | File Templates.
 */

import org.codehaus.yagll.context.*

public class Unbind {

  static void unbind(name) {
    LdapUtils.unbind(name)
  }
  
}