package com.perfectworldprogramming.mobile.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to put on the Primary Key field of a domain object.
 *
 * It is best to make the property to be a Long.
 *
 * The generated Primary Key field in the database will be an auto-increment field.
 * 
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 9:44 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ColumnSetter
public @interface PrimaryKey {
    public String value() default "";
}
