package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
/**
 * {@link ColumnTypeMapper} implementation for {@link Boolean} fields, which maps boolean values in the model
 * layer to a 0|1 {@code INTEGER} in the database.
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 06/06/2012
 *
 */
public class BooleanMapper extends AbstractMapper<Boolean>
{
    public static final ColumnTypeMapper<Boolean> INSTANCE = new BooleanMapper();

    @Override
    public void modelToDatabase(Field field, Object instance, ContentValues values)
    {
        field.setAccessible(true);
        try
        {
            final Column column = field.getAnnotation(Column.class);
            if(Boolean.class.equals(field.getType()))
            {
                Boolean value = (Boolean)field.get(instance);
                if(value!=null)
                {
                    values.put(column.value(), value ? Integer.valueOf(1): Integer.valueOf(0));
                }
            }
            else
            {
                values.put(column.value(), field.getBoolean(instance) ? Integer.valueOf(1): Integer.valueOf(0));
            }
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Unable to get the Column value from the domain object: " + instance.getClass().getName() + " for Field: "
                    + field.getName());
        }
        catch(IllegalArgumentException e)
        {
            throw e;
        }
    }

    @Override
    public void databaseToModel(Cursor cursor, Field field, Object instance)
    {
        field.setAccessible(true);
        final Column column = field.getAnnotation(Column.class);
        final int columnIndex = cursor.getColumnIndex(column.value());
        if(cursor.isNull(columnIndex))
        {
            return;
        }

        final Boolean value = Boolean.valueOf(cursor.getInt(columnIndex) == 1);
        try
        {
            field.set(instance, value);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public String getDatabaseColumnType()
    {
        return "INTEGER";
    }

    @Override
    public Object asSqlQueryParameter(Boolean value)
    {
        return (Boolean)value ? Integer.valueOf(1): Integer.valueOf(0);
    }

    
}
