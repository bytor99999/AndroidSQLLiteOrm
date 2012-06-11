package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.FieldNotFoundException;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;

/**
 * In many situations the default mapping to the database can use
 * {@code object.toString()} and rely on SQLite to fill in the gaps.
 * Note that this is for use with {@link Column} annotated fields only,
 * for Primary Keys see {@link PrimaryKeyMapper}
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 06/06/2012
 * 
 */
public abstract class AbstractMapper<T> implements ColumnTypeMapper<T>
{
    /**
     * Convenience method for {@link CursorExtractor}s that know the column name
     * but not the {@link Field}
     * 
     * @param object
     * @param columnName
     * @param values
     */
    @Override
    public void modelToDatabase(String columnName, Object object, ContentValues values)
    {
        modelToDatabase(this.getFieldFromColumnName(columnName, object), object, values);
    }

    @Override
    public void modelToDatabase(Field field, Object instance, ContentValues values)
    {
        field.setAccessible(true);
        try
        {
            Object value = field.get(instance);
            if (value != null)
            {
                Column column = field.getAnnotation(Column.class);
                values.put(column.value(), value.toString());
            }
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Unable to get the Column value from the domain object: " + instance.getClass().getName() + " for Field: "
                    + field.getName());
        }

    }

    /**
     * Convenience method for {@link CursorExtractor}s that know the column name
     * but not the {@link Field}
     * 
     * @param object
     * @param columnName
     * @param values
     */
    @Override
    public void databaseToModel(Cursor cursor, String columnName, Object object)
    {
        databaseToModel(cursor, this.getFieldFromColumnName(columnName, object), object);
    }

    @Override
    public abstract void databaseToModel(Cursor cursor, Field field, Object instance);

    @Override
    public abstract String getDatabaseColumnType();

    @Override
    public Object asSqlQueryParameter(T value)
    {
        return value.toString();
    }

    private Field getFieldFromColumnName(String columnName, Object object)
    {
        Class<? extends Object> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            if(field.isAnnotationPresent(Column.class))
            {
                Column column = field.getAnnotation(Column.class);
                if (columnName.equalsIgnoreCase(column.value()))
                {
                    return field;
                }
            }
        }
        
        throw new FieldNotFoundException("Could not find field for column name " + columnName + " in type " + clazz.getName());
    }
}
