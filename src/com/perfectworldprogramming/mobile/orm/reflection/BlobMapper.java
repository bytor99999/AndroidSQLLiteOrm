package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;
import java.sql.Blob;

import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;

/**
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 06/06/2012
 * 
 */
public class BlobMapper extends AbstractMapper<Blob>
{
    public static final ColumnTypeMapper<Blob> INSTANCE = new BlobMapper();

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
            field.set(instance, cursor.getBlob(columnIndex));
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getDatabaseColumnType()
    {
        return "INTEGER";
    }

}
