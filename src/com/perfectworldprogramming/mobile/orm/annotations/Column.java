package com.perfectworldprogramming.mobile.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to place of Domain object properties to map them to a field
 * in a database table.
 *
 * User: Mark Spritzler
 * Date: 3/10/11
 * Time: 8:36 PM
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ColumnSetter()
public @interface Column {
    /**
     * Column name in the database. This will be used in the CREATE statements
     * and for setting the domain property to the value of the column in the database.
     *
     * @return String field name in the database
     */
    public String value() default "";

    /**
     * Should the column be unique and not allow duplicates.
     *
     * default value is false, allowing for duplicates in the database.
     *
     * @return boolean
     */
    public boolean unique() default false;

    /**
     * Should the column allow nulls. True allows nulls, False does not allow nulls
     *
     * default value is true, allowing for nulls.
     *
     * @return boolean
     */
    public boolean nullable() default true;

    /**
     * Used to determine the field type to use in the CREATE statement.
     * 
     * @return ColumnType
     */
    public ColumnType type() default ColumnType.TEXT;
}
