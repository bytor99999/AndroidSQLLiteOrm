package com.perfectworldprogramming.mobile.orm.interfaces;

import java.util.List;

import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.NoRowsReturnedException;

/**
 * Interface for the Android Template class. Since there is only one template
 * class this interface isn't as necessary, but created to be as close a match to
 * JDBCTemplate in Core Spring. So if you have used JDBCTemplate before, you will
 * be completely familiar with the AndroidSQLiteTemplate class with same method
 * names and functionality.
 *
 * User: Mark Spritzler
 * Date: 3/19/11
 * Time: 2:31 PM
 */
public interface JdbcOperations {

    /**
     * Creates an insert statement and inserts the data in the Domain object passed in
     * as a parameter.
     *
     * The domain object passed in must mapped with the @PrimaryKey, @Column and @ForeignKey
     * annotations. If your domain object is not mapped and you want to pass in an insert sql
     * statement look at
     * @param object domain object without an ID, and not inserted in the database yet
     * @return long, number of rows inserted. Should return 1 if successful
     */
    public long insert(Object object) throws DataAccessException;

    /**
     * Handles insert SQL statement to the database.
     * Use this method instead of insert(Object object
     * if your domain objects are not mapped with the ORM Annotations.
     *
     * @param sql Insert sql statement
     * @param args values to replace parameters within the insert statement
     * @return long, number of rows inserted. Should return 1 if successful
     */
    public long insert(String sql, Object... args) throws DataAccessException;

    /**
     * Creates an update statement and updates the data in the Domain object passed in
     * as a parameter.
     *
     * The domain object passed in must mapped with the @PrimaryKey, @Column and @ForeignKey
     * annotations. If your domain object is not mapped and you want to update the data
     * look at update(String sql, Object... args
     *
     * @param object domain object without an ID, and not inserted in the database yet
     * @return long, number of rows updated. Should return 1 if successful
     */
    public long update(Object object) throws DataAccessException;

    /**
     * Handles updates to the database.
     * 
     *
     * Use this method instead of update(Object object)
     * if your domain objects are not mapped with the ORM Annotations.
     *
     * @param sql update statement SQL
     * @param args values to set the parameters in the update statement
     */
    public void update(String sql, Object... args) throws DataAccessException;

    /**
     *
     * @param object
     * @return
     */
    public long delete(Object object) throws DataAccessException;

    /**
     * Delete a single row in a table based on a single value for a single field.
     * 
     * While this will call delete(table, whereClause, whereArgs) on the underlying SQLiteDatabase object, it will not allow an array of ids
     * or a where clause. If you want that functionality use {@link #delete(String, Object...)} template method instead.
     *  
     * @param table table to delete from
     * @param columnName field to use in the where clause of the delete statement
     * @param columnValue primary key value
     * @return long should return 1 if successful
     */
    public long delete(String table, String columnName, String columnValue) throws DataAccessException;

    /**
     * 
     * @param sql
     * @param args
     */
    public void delete(String sql, Object... args) throws DataAccessException;
    
    /**
     * Runs a query where one row with one field is expected in results, where
     * the field value is an int.
     * 
     * However, if the type of that field is not an int, then 0 will always be returned.
     * This is because of the underlying Android SQLite implementation of running a simple
     * query for int.
     * 
     * If the result returns more than one row, the first row's data will be returned.
     * If the result returned has zero rows, then a DataAccessException is thrown.
     *
     * @param sql select statement sql with one field in the select portion
     * @param args values to set the parameters in the update statement
     * @return int the value of the field. 0 if the field type is not a number
     * @throws DataAccessException when the result set returned zero rows
     */
    public int queryForInt(String sql, Object... args) throws DataAccessException;

    /**
     * Runs a query where one row with one field is expected in results, where
     * the field value is a long.
     * 
     * However, if the type of that field is not an long, then 0 will always be returned.
     * This is because of the underlying Android SQLite implementation of running a simple
     * query for long.
     * 
     * If the result returns more than one row, the first row's data will be returned.
     * If the result set returned has zero rows, then a NoRowsReturnedException is thrown.
     *
     * @param sql select statement sql with one field in the select portion
     * @param args values to set the parameters in the update statement
     * @return long the value of the field. 0 if the field type is not a number
     * @throws NoRowsReturnedException when the result set returned zero rows
     */
    public long queryForLong(String sql, Object... args) throws DataAccessException;

    /**
     * Runs a query where one row with one field is expected in results, where
     * the field value is a String.
     * 
     * However, if the type of that field is not an long, then SQLite will convert it to a String.
     * This is because of the underlying Android SQLite implementation of running a simple
     * query for String.
     * 
     * If the result returns more than one row, the first row's data will be returned.
     * If the result set returned has zero rows, then a NoRowsReturnedException is thrown.
     *
     * @param sql select statement sql with one field in the select portion
     * @param args values to set the parameters in the update statement
     * @return String the value of the field. if the field type is not a string the type is converted into a String
     * @throws NoRowsReturnedException when the result set returned zero rows
     */
    public String queryForString(String sql, Object... args) throws DataAccessException, NoRowsReturnedException;

    /**
     *
     * Runs a query where one row expected in results
     * 
     * However, if the type of that field is not an long, then SQLite will convert it to a String.
     * This is because of the underlying Android SQLite implementation of running a simple
     * query for String.
     * 
     * If the result returns more than one row, the first row's data will be returned.
     * If the result set returned has zero rows, then a NoRowsReturnedException is thrown.
     * 
     * @param sql select statement sql with one field in the select portion
     * @param clazz Class of the domain object to create with the results from the Cursor
     * @param args values to set the parameters in the update statement
     * @param <T> Type of the Domain object that is returned
     * @return the domain object filled with the values from the first row of the Cursor
     * @throws NoRowsReturnedException when the result set returned zero rows
     */
    public <T> T queryForObject(String sql, Class<T> clazz, Object... args) throws NoRowsReturnedException;

    /**
     *
     * @param sql
     * @param cursorRowMapper
     * @param args
     * @param <T>
     * @return
     */
    public <T> T queryForObject(String sql, CursorRowMapper<T> cursorRowMapper, Object... args) throws DataAccessException;

    /**
     *
     * @param sql
     * @param cursorExtractor
     * @param args
     * @param <T>
     * @return
     */
    public <T> T queryForObject(String sql, CursorExtractor<T> cursorExtractor, Object... args) throws DataAccessException;

    /**
     *
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findById(Class<T> clazz, Long id) throws DataAccessException;

    /**
     *
     * @param sql
     * @param clazz
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> query(String sql, Class<T> clazz, Object... args) throws DataAccessException;

    /**
     *
     * @param sql
     * @param cursorRowMapper
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> query(String sql, CursorRowMapper<T> cursorRowMapper, Object... args) throws DataAccessException;

}
