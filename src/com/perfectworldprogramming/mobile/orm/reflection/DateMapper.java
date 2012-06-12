package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;
import java.sql.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
/**
 * {@link ColumnTypeMapper} implementation for {@link Date} fields, which maps Date values in the model
 * layer to {@code INTEGER} in the database.
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 06/06/2012
 *
 */
public class DateMapper extends AbstractMapper<Date>
{
    public static final ColumnTypeMapper<Date> INSTANCE = new DateMapper();

    @Override
    public void modelToDatabase(Field field, Object instance, ContentValues values)
    {
        field.setAccessible(true);
        try
        {
            Date value = (Date)field.get(instance);
            if(value!=null)
            {
                final Column column = field.getAnnotation(Column.class);
                values.put(column.value(), value.getTime());
            }
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Unable to get the Column value from the domain object: " + instance.getClass().getName() + " for Field: "
                    + field.getName());
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

        final Date value = new Date(cursor.getLong(columnIndex));
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
    public Object asSqlQueryParameter(Date value)
    {
        if(value!=null)
        {
            return Long.valueOf(((Date)value).getTime());
        }
        return null;
    }
}
