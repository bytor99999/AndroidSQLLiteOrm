package com.perfectworldprogramming.mobile.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ForeignKey;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.annotations.Transient;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.ExtraResultsException;
import com.perfectworldprogramming.mobile.orm.exception.InvalidCursorException;
import com.perfectworldprogramming.mobile.orm.exception.InvalidCursorRowMapperException;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.reflection.PrimaryKeyMapper;

/**
 * User: Mark Spritzler
 * Date: 3/12/11
 * Time: 9:45 PM
 */
public class CursorAdapter {

    /**
     * convert a Cursor with a multitude of date rows and return a List of instances of Class
     * That is mapped to the corresponding database table that the Cursor has data from
     *
     * @param cursor returned data from the database query
     * @param clazz Domain object Class to put all the data from the cursor
     * @param <T> Domain Object type
     * @return List<T> returns a list of populated domain objects based on the cursor data.
     */
    public <T> List<T> adaptListFromCursor(Cursor cursor, Class<T> clazz) {
        List<T> results = new ArrayList<T>();
        if (cursor.getCount() > 0) {
            try {
                results = getValuesFromCursor(cursor, clazz);
            }
            finally {
                cursor.close();
            }
        }
        return results;
    }

    /**
     * convert a Cursor with a multitude of data rows and return a List of instances by calling
     * the CursorRowMapper passed in. This will loop through all the rows of the Cursor and pass
     * each row to the CursorRowMapper.
     *
     * @param cursor returned data from the database query.
     * @param cursorRowMapper CursorRowMapper that implements mapRow to convert a row in the cursor to a <T> instance
     * @param <T> Domain Object type the CursorRowMapper maps
     * @return List<T> returns a list of populated domain objects based on the cursor data.
     */
    public <T> List<T> adaptListFromCursor(Cursor cursor, CursorRowMapper<T> cursorRowMapper) {
        List<T> values = new ArrayList<T>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        T newInstance = cursorRowMapper.mapRow(cursor, cursor.getPosition());
                        values.add(newInstance);
                    } catch (IllegalStateException ise) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                        }
                        throw new InvalidCursorRowMapperException(cursorRowMapper.getClass());
                    }
                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return values;
    }

    /**
     * Returns a Single Domain Object from the Cursor's first row. All other rows will be ignored.
     * Always moves to the first row and always closes the cursor.
     * @param cursor returned data from the database query.
     * @param clazz Domain object Class to put all the data from the cursor
     * @param <T> Domain Object type of the Class
     * @return T domain object populated with data from the first row in the Cursor
     */
    public <T> T adaptFromCursor(Cursor cursor, Class<T> clazz) {
        T newInstance = null;
        if (cursor != null) {
            try
            {
                if (cursor.moveToFirst()) {
                    newInstance = adaptCurrentFromCursor(cursor, clazz);
                }
            }
            finally
            {
                cursor.close();
            }
        }
        return newInstance;
    }

    /**
     * Checks that the cursor is not {@code beforeFirst} or {@code afterLast} positions but does not move
     * or close the cursor.
     * @param cursor
     * @param clazz
     * @param willClose
     * @return
     */
    public <T> T adaptCurrentFromCursor(Cursor cursor, Class<T> clazz) {
        T newInstance = null;
        if (cursor != null) {
             if (!cursor.isBeforeFirst() && !cursor.isAfterLast()) {
                 newInstance = getSingleObjectValuesFromCursor(cursor, clazz);
             }
        }
        return newInstance;
    }

    public <T> T adaptFromCursor(Cursor cursor, CursorRowMapper<T> cursorRowMapper) {
        T newInstance = null;
        if (cursor != null) {
            if(cursor.getCount() != 1) {
                throw new ExtraResultsException(cursor.getCount());
            }

            if (cursor.moveToFirst()) {
                try {
                    newInstance = cursorRowMapper.mapRow(cursor, 1);
                } catch (IllegalStateException ise) {
                    throw new InvalidCursorRowMapperException(cursorRowMapper.getClass());
                } finally {
                    if (!cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        }
        return newInstance;
    }

    /**
     * Converts the data from the Cursor into a Domain object based on the CursorExtractor callback.
     *
     * A CursorExtractor receives the entire Cursor, and is responsible for looping through the
     * dataset and created the more complex domain object tree graph.
     * 
     * @param cursor returned data from the database query.
     * @param cursorExtractor Callback interface that receives the entire Cursor
     * @param <T> Domain Object type of the Class
     * @return T Domain Object fully populated with data from the Cursor
     */
    public <T> T adaptFromCursor(Cursor cursor, CursorExtractor<T> cursorExtractor) {
        T result = null;
        try {
            result = cursorExtractor.extractData(cursor);
        } catch (IllegalStateException ise) {
            throw new InvalidCursorException(cursorExtractor.getClass());
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    private <T> List<T> getValuesFromCursor(Cursor cursor, Class<T> clazz) {
        List<T> values = new ArrayList<T>();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        T newInstance = getSingleObjectValuesFromCursor(cursor, clazz);
                        values.add(newInstance);
                    } while (cursor.moveToNext());
                }
            }
            finally {
                cursor.close();
            }
        }
        return values;
    }

    private <T> T getSingleObjectValuesFromCursor(Cursor cursor, Class<T> clazz) {
        T newInstance = null;
        try {
            newInstance = clazz.newInstance();
            setFieldValues(clazz, newInstance, cursor);
        } catch (InstantiationException e) {
            throw new RuntimeException("CursorAdapter", e);
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("CursorAdapter", e);
            //e.printStackTrace();
        }
        return newInstance;
    }

    private <T> void setFieldValues(Class<? extends Object> clazz, T object, Cursor cursor) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            setFieldValue(field, object, cursor);
        }
    }

	private <T> void setFieldValue(Field fieldToSet, T object, Cursor cursor) {
        //Skip over @Transient, @ForeignKey and static fields
        if (fieldToSet.isAnnotationPresent(Transient.class) ||
                fieldToSet.isAnnotationPresent(ForeignKey.class)
                || ((fieldToSet.getModifiers()&Modifier.STATIC)!=0)) {
            return;
        }
        final String value;
        final ColumnTypeMapper<?> mapper;
        if (fieldToSet.isAnnotationPresent(PrimaryKey.class) ) {
            mapper = PrimaryKeyMapper.INSTANCE;
            value = fieldToSet.getAnnotation(PrimaryKey.class).value();
        }
        else if (fieldToSet.isAnnotationPresent(Column.class)){
			mapper = fieldToSet.getAnnotation(Column.class).type().getMapper();
			value = fieldToSet.getAnnotation(Column.class).value();
        }
        else
        {
            throw new DataAccessException("Invalid type, cannot find field "+fieldToSet.getName()+" on type "+object.getClass().getName());
        }
        if(-1==cursor.getColumnIndex(value))
        {
            throw new InvalidCursorException(object.getClass());
        }
        mapper.databaseToModel(cursor, fieldToSet, object);
    }

}
