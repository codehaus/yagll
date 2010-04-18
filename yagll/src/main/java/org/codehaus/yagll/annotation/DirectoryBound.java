package org.codehaus.yagll.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 12:07:51 AM
 * To change this template use File | Settings | File Templates.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectoryBound {
    public String[] entities() default {};
    public String[] objectClasses() default {};
}
