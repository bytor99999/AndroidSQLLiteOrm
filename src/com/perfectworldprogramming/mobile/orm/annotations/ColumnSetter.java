package com.perfectworldprogramming.mobile.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Mark Spritzler
 * Date: 3/23/11
 * Time: 6:33 PM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnSetter {
    public String value() default "";
}
