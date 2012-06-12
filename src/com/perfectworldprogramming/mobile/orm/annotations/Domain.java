package com.perfectworldprogramming.mobile.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.perfectworldprogramming.mobile.orm.helper.AndroidSQLLiteOpenHelper;

/**
 * Annotation defining domain objects and meta information used in the database mapping
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 12/06/2012
 *
 * @see {@link AndroidSQLLiteOpenHelper}
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Domain {

    /**
     * Minimum database version required to support this type
     * @return the database version
     */
    public int databaseVersion() default 1;

    /**
     * Database instance used to store this type. Types with the same name will
     * be stored in the same database file and accessed via the same {@link SQLiteOpenHelper}
     * @return the database name
     */
    public String databaseName();

}
