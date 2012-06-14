package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;

import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;

/**
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 06/06/2012
 * 
 */
public class DoubleMapper extends AbstractMapper<Double>
{
    public static final ColumnTypeMapper<Double> INSTANCE = new DoubleMapper();

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

        try
        {
            field.set(instance, Double.valueOf(cursor.getDouble(columnIndex)));
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getDatabaseColumnType()
    {
        return "REAL";
    }

}
