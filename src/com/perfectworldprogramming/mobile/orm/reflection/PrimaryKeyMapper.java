package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.FieldNotFoundException;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
/**
 * {@link ColumnTypeMapper} for the {@link PrimaryKey} annotated column
 * 
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 06/06/2012
 *
 */
public class PrimaryKeyMapper implements ColumnTypeMapper<Long>
{
    public static final ColumnTypeMapper<Long> INSTANCE = new PrimaryKeyMapper();

    @Override
    public void modelToDatabase(String columnName, Object object, ContentValues values)
    {
        Field field = this.getFieldFromColumnName(columnName, object);
        if(field==null || !field.isAnnotationPresent(PrimaryKey.class) || !columnName.equalsIgnoreCase(field.getAnnotation(PrimaryKey.class).value()))
        {
            throw new FieldNotFoundException("Could not find primary key field for column name " + columnName + " in type " + object.getClass().getName());
        }
        modelToDatabase(field, object, values);
    }

    @Override
    public void modelToDatabase(Field field, Object instance, ContentValues values)
    {
        field.setAccessible(true);
        try
        {
            final PrimaryKey pk = field.getAnnotation(PrimaryKey.class);
            if(Long.class.equals(field.getType()))
            {
                Long value = (Long)field.get(instance);
                if(value!=null)
                {
                    values.put(pk.value(), value);
                }
            }
            else
            {
                values.put(pk.value(), field.getLong(instance) );
            }
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Unable to get the Column value from the domain object: " + instance.getClass().getName() + " for Field: "
                    + field.getName());
        }
    }

    @Override
    public void databaseToModel(Cursor cursor, String columnName, Object object)
    {
        Field field = this.getFieldFromColumnName(columnName, object);
        if(!field.getName().equals(columnName))
        {
            throw new FieldNotFoundException("Could not find primary key field for column name " + columnName + " in type " + object.getClass().getName());
        }
        databaseToModel(cursor, field, object);
    }

    @Override
    public void databaseToModel(Cursor cursor, Field field, Object instance)
    {
        field.setAccessible(true);
        final PrimaryKey pk = field.getAnnotation(PrimaryKey.class);
        final int columnIndex = cursor.getColumnIndex(pk.value());

        try
        {
            field.set(instance, cursor.getLong(columnIndex));
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
    public Object asSqlQueryParameter(Long value)
    {
        return value.toString();
    }

    private Field getFieldFromColumnName(String columnName, Object object)
    {
        Class<? extends Object> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            PrimaryKey pk = field.getAnnotation(PrimaryKey.class);
            if(pk!=null)
            {
                return field;
            }
        }
        
        throw new FieldNotFoundException("Could not find primary key field for column name " + columnName + " in type " + clazz.getName());
    }
}
