package com.perfectworldprogramming.mobile.orm.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ForeignKey;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.annotations.Transient;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.FieldNotFoundException;
import com.perfectworldprogramming.mobile.orm.exception.NoPrimaryKeyFieldException;

/**
 * User: Mark Spritzler Date: 3/12/11 Time: 9:48 PM
 */
public class DomainClassAnalyzer
{

    public Object mapQueryParameter(Object value, Class<?> clazz, String columnName)
    {
        for (Field field : clazz.getDeclaredFields())
        {
            // skip transient and static
            if(field.isAnnotationPresent(Transient.class))
            {
                continue;
            }
            field.setAccessible(true);

            if (field.isAnnotationPresent(PrimaryKey.class))
            {
                PrimaryKey pk = field.getAnnotation(PrimaryKey.class);
                if (pk.value().equalsIgnoreCase(columnName))
                {
                    return PrimaryKeyMapper.INSTANCE.asSqlQueryParameter((Long)value);
                }
            }

            if(field.isAnnotationPresent(ForeignKey.class))
            {
                ForeignKey fk = field.getAnnotation(ForeignKey.class);
                if (fk.value().equalsIgnoreCase(columnName))
                {
                    return PrimaryKeyMapper.INSTANCE.asSqlQueryParameter((Long)value);
                }
            }
            
            if(field.isAnnotationPresent(Column.class))
            {
                Column column = field.getAnnotation(Column.class);
                if(column.value().equalsIgnoreCase(columnName))
                {
                    return column.type().getMapper().asSqlQueryParameter(value);
                }
            }
        }
        throw new FieldNotFoundException("Could not find database field "+columnName+" in " + clazz.getName());
    }

    public Field getPrimaryKeyField(Class<? extends Object> clazz)
    {
        Field primaryKeyField = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null)
            {
                primaryKeyField = field;
                break;
            }
        }
        if (primaryKeyField == null)
        {
            throw new NoPrimaryKeyFieldException(clazz);
        }
        return primaryKeyField;
    }

    public String getPrimaryKeyFieldName(Class<? extends Object> clazz)
    {
        PrimaryKey primaryKey = getPrimaryKey(clazz);
        return primaryKey.value();
    }

    public PrimaryKey getPrimaryKey(Class<? extends Object> clazz)
    {
        return getPrimaryKeyField(clazz).getAnnotation(PrimaryKey.class);
    }

    public List<Field> getForeignKeyFields(Class<? extends Object> clazz)
    {
        List<Field> foreignKeyFields = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
            if (foreignKey != null)
            {
                foreignKeyFields.add(field);
                break;
            }
        }
        return Collections.unmodifiableList(foreignKeyFields);
    }

    public long getIdFromObject(Object o)
    {
        Long id = -1l;
        Class<? extends Object> clazz = o.getClass();
        Field primaryKeyField = getPrimaryKeyField(clazz);
        primaryKeyField.setAccessible(true);
        try
        {
            id = (Long) primaryKeyField.get(o);
            if (id == null)
            {
                throw new DataAccessException("Domain classes must have an @PrimaryKey property in order to use the ORM functionality. Your "
                        + o.getClass().getName() + " class is missing @PrimaryKey");
            }
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        return id;
    }

    public void setIdToNewObject(Object object, long id)
    {
        Field fieldToSet = this.getPrimaryKeyField(object.getClass());
        fieldToSet.setAccessible(true);
        try
        {
            fieldToSet.set(object, id);
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ContentValues createContentValues(Object object)
    {
        Class<? extends Object> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        ContentValues values = new ContentValues(fields.length);
        for (Field field : fields)
        {
            Column column = field.getAnnotation(Column.class);
            if (column != null)
            {
                column.type().getMapper().modelToDatabase(field, object, values);
            }
            else
            {
                ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
                if (foreignKey != null)
                {
                    addForeignKeyValuesToContentValues(field, foreignKey, values, object);
                }
            }
        }
        return values;
    }

    private void addForeignKeyValuesToContentValues(Field field, ForeignKey foreignKey, ContentValues values, Object domainObject)
    {
        field.setAccessible(true);
        try
        {
            Object foreignDomainObject = field.get(domainObject);
            if (foreignDomainObject != null)
            {
                Object value = getIdFromObject(foreignDomainObject);
                if (value != null)
                {
                    values.put(foreignKey.value(), value.toString());
                }
            }
        }
        catch (IllegalAccessException e)
        {
            throw new DataAccessException("Unable to get the Foreign Key value from the domain object: " + domainObject.getClass().getName());
        }
    }
    
}
