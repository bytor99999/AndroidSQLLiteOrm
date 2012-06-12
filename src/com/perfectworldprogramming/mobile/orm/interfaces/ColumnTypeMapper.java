package com.perfectworldprogramming.mobile.orm.interfaces;

import java.lang.reflect.Field;

import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;

import android.content.ContentValues;
import android.database.Cursor;

public interface ColumnTypeMapper<T>
{
    void modelToDatabase(String columnName, Object object, ContentValues values);
    void modelToDatabase(Field field, Object instance, ContentValues values);
    void databaseToModel(Cursor cursor, String columnName, Object instance);
    void databaseToModel(Cursor cursor, Field field, Object instance);
    String getDatabaseColumnType();
    /**
     * Returns the value on the {@code object} converted to a
     * form suitable for use as an SQL query parameter.
     * 
     * @see AndroidSQLiteTemplate
     * @param object
     * @param field
     * 
     * @return
     */
    Object asSqlQueryParameter(T input);
}
