package com.perfectworldprogramming.mobile.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to put on a ManyToOne association between domain objects.
 *
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 9:21 PM
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ColumnSetter
public @interface ForeignKey {
    public String value() default "";
}
