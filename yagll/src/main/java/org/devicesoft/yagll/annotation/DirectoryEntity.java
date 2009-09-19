package org.devicesoft.yagll.annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 4:30:18 AM
 * To change this template use File | Settings | File Templates.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectoryEntity {
    public String entity();
    public String attribute();
    public boolean isByteArray() default false;
    public boolean mustBeUnique() default false;
}
