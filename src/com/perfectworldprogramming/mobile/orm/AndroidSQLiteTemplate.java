package com.perfectworldprogramming.mobile.orm;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.EmptySQLStatementException;
import com.perfectworldprogramming.mobile.orm.exception.NoRowsReturnedException;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.interfaces.JdbcOperations;
import com.perfectworldprogramming.mobile.orm.reflection.DomainClassAnalyzer;

/**
 * For queries that return Cursors will use the CursorAdapter class to convert
 * the Cursor into your Domain object, either via Annotations you put in your
 * domain objects to map to the tables, or via CursorRowMapper or
 * CursorExtractor implementations. Either approach, the CursorAdapter will
 * close the Cursor after it has completed conversion. No need to close Cursors
 * yourself.
 * 
 * User: Mark Spritzler Date: 3/10/11 Time: 10:07 PM
 */
public class AndroidSQLiteTemplate implements JdbcOperations {
	private SQLiteDatabase sqLiteDatabase;
	protected final DomainClassAnalyzer domainClassAnalyzer = new DomainClassAnalyzer();
	private final CursorAdapter cursorAdapter = new CursorAdapter();

	public AndroidSQLiteTemplate(SQLiteDatabase sqLiteDatabase) {
		this.sqLiteDatabase = sqLiteDatabase;
	}

	@Override
	public long insert(Object object) {
		ContentValues values = domainClassAnalyzer.createContentValues(object);
		long id = sqLiteDatabase.insert(object.getClass().getSimpleName(),
				null, values);
		// the insert method used above doesn't throw an exception if it can't
		// insert, but returns -1 as the result.
		if (id != -1) {
			domainClassAnalyzer.setIdToNewObject(object, id);
		} else {
			throw new DataAccessException(
					"Unable to insert new record for object: " + object);
		}
		return id;
	}

	@Override
	public long insert(String sql, Object... args) {
		sql = replaceParametersInStatement(sql, args);
		SQLiteStatement statement = null;
		Long results;
		try {
			statement = sqLiteDatabase.compileStatement(sql);
			results = statement.executeInsert();
		} catch (SQLException se) {
			throw new DataAccessException(se.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return results;
	}

	@Override
	public long update(Object object) {
		ContentValues values = domainClassAnalyzer.createContentValues(object);
		String whereClause = domainClassAnalyzer.getPrimaryKeyFieldName(object
				.getClass()) + " = ?";
		String[] whereArgs = { String.valueOf(domainClassAnalyzer
				.getIdFromObject(object)) };

		return sqLiteDatabase.update(object.getClass().getSimpleName(), values,
				whereClause, whereArgs);
	}

	@Override
	public void update(String sql, Object... args) {
		sql = replaceParametersInStatement(sql, args);
		SQLiteStatement statement = null;
		try {
			statement = sqLiteDatabase.compileStatement(sql);
			statement.execute();
		} catch (SQLException se) {
			throw new DataAccessException(se.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public long delete(Object object) {
		if (object == null) {
			throw new DataAccessException(
					"Please supply a non null domain object to delete(Object) method");
		}
		String whereClause = domainClassAnalyzer.getPrimaryKeyFieldName(object
				.getClass()) + " = ?";
		String[] whereArgs = { String.valueOf(domainClassAnalyzer
				.getIdFromObject(object)) };
		long results = -1;
		try {
			results = sqLiteDatabase.delete(object.getClass().getSimpleName(),
					whereClause, whereArgs);
			if (results == 0) {
				throw new DataAccessException(
						"unable to delete "
								+ object.getClass().getName()
								+ ". Most likely the Primary Key id value does not exist in the database.");
			}
		} catch (SQLiteException sle) {

		}
		return results;
	}

	@Override
	public long delete(String table, String columnName, String columnValue) {
		if (table == null || columnName == null || columnValue == null
				|| "".equals(table) || "".equals(columnName)
				|| "".equals(table)) {
			throw new DataAccessException(
					"Please supply tableName, columnName and columnValue in order to delete any data");
		}
		String[] args = { columnValue };
		long results = -1;
		try {
			results = sqLiteDatabase
					.delete(table, "" + columnName + "=?", args);
		} catch (SQLiteException sle) {
			throw new DataAccessException(
					"Call to delete(String, String, String) failed. Please check you are passing in correct tableName, columnName and value to this method");
		}
		return results;
	}

	@Override
	public void delete(String sql, Object... args) {
		update(sql, args);
	}

	@Override
	public int queryForInt(String sql, Object... args) {
		return (int) queryForLong(sql, args);
	}

	@Override
	public long queryForLong(String sql, Object... args) {
		sql = replaceParametersInStatement(sql, args);
		SQLiteStatement statement = null;
		long results;
		try {
			statement = sqLiteDatabase.compileStatement(sql);
			results = statement.simpleQueryForLong();
		} catch (SQLiteDoneException se) {
			throw new NoRowsReturnedException(
					"Query For Long/Int did not return any rows for sql: "
							+ sql);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return results;
	}

	@Override
	public String queryForString(String sql, Object... args) {
		sql = replaceParametersInStatement(sql, args);
		SQLiteStatement statement = null;
		String results;
		try {
			statement = sqLiteDatabase.compileStatement(sql);
			results = statement.simpleQueryForString();
		} catch (SQLiteDoneException se) {
			throw new NoRowsReturnedException(
					"Query For String did not return any rows for sql: " + sql);
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return results;
	}

	// ** All Implementations below call their corresponding executeFor* private
	// methods for a more central exception handling
	@Override
	public <T> T queryForObject(String sql, CursorRowMapper<T> cursorRowMapper,
			Object... args) {
		if (cursorRowMapper == null) {
			throw new IllegalArgumentException();
		}
		sql = replaceParametersInStatement(sql, args);
		return executeForSingleObject(sql, cursorRowMapper);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> clazz, Object... args) {
		sql = replaceParametersInStatement(sql, args);
		T t = executeForSingleObject(sql, clazz);
		if (t == null) {
			throw new DataAccessException(
					"QueryForObject returned no results for query: " + sql);
		}
		return t;
	}

	@Override
	public <T> T queryForObject(String sql, CursorExtractor<T> cursorExtractor,
			Object... args) {
		if (cursorExtractor == null) {
			throw new IllegalArgumentException();
		}
		sql = replaceParametersInStatement(sql, args);
		return executeForSingleObject(sql, cursorExtractor);
	}

	@Override
	public <T> T findById(Class<T> clazz, Long id) {
		String sql = generateStatementById(clazz, id);
		return executeForSingleObject(sql, clazz);
	}

	@Override
	public <T> List<T> query(String sql, Class<T> clazz, Object... args) {
		sql = replaceParametersInStatement(sql, args);
		return executeForList(sql, clazz);
	}

	@Override
	public <T> List<T> query(String sql, CursorRowMapper<T> cursorRowMapper,
			Object... args) {
		if (cursorRowMapper == null) {
			throw new IllegalArgumentException();
		}
		sql = replaceParametersInStatement(sql, args);
		return executeForList(sql, cursorRowMapper);
	}
	
    public Object mapQueryParameter(Object value, Class<?> clazz, String columnName)
    {
        return this.domainClassAnalyzer.mapQueryParameterByColumnName(value, clazz, columnName);
    }

	private <T> T executeForSingleObject(String sql,
			CursorExtractor<T> cursorExtractor) {
		if (sql == null || "".equals(sql)) {
			throw new EmptySQLStatementException();
		}

		if (cursorExtractor == null) {
			throw new IllegalArgumentException();
		}

		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.rawQuery(sql, null);
			return cursorAdapter.adaptFromCursor(cursor, cursorExtractor);
		} catch (SQLiteException se) {
			throw new DataAccessException(se.getMessage());
		}
	}

	private <T> T executeForSingleObject(String sql,
			CursorRowMapper<T> cursorRowMapper) {
		if (cursorRowMapper == null) {
			throw new IllegalArgumentException();
		}
		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.rawQuery(sql, null);
			return cursorAdapter.adaptFromCursor(cursor, cursorRowMapper);
		} catch (SQLiteException se) {
			throw new DataAccessException(se.getMessage());
		}
	}

	private <T> T executeForSingleObject(String sql, Class<T> clazz) {
		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.rawQuery(sql, null);
			return cursorAdapter.adaptFromCursor(cursor, clazz);
		} catch (SQLiteException se) {
			throw new DataAccessException(se.getMessage());
		}
	}

	private <T> List<T> executeForList(String sql,
			CursorRowMapper<T> cursorRowMapper) {
		if (cursorRowMapper == null) {
			throw new IllegalArgumentException();
		}
		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.rawQuery(sql, null);
			return cursorAdapter.adaptListFromCursor(cursor, cursorRowMapper);
		} catch (SQLiteException se) {
			throw new DataAccessException(se.getMessage());
		} catch (Exception e) {
			throw new DataAccessException(
					"Unable to execute query. Please check your sql for incorrect sql grammer.");
		}
	}

	private <T> List<T> executeForList(String sql, Class<T> clazz) {
		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.rawQuery(sql, null);
			return cursorAdapter.adaptListFromCursor(cursor, clazz);
		} catch (SQLiteException se) {
			throw new DataAccessException(se.getMessage());
		}
	}

	private String generateStatementById(Class<? extends Object> clazz, Long id) {
		StringBuilder selectStatement = new StringBuilder("SELECT * FROM ");
		selectStatement.append(clazz.getSimpleName());
		selectStatement.append(" WHERE ");
		selectStatement.append(domainClassAnalyzer
				.getPrimaryKeyFieldName(clazz));
		selectStatement.append(" = ");
		selectStatement.append(id);

		return selectStatement.toString();
	}

	private String replaceParametersInStatement(String sql, Object... args) {
		if (sql == null || "".equals(sql)) {
			throw new EmptySQLStatementException();
		}

		if (args != null) {
			for (Object arg : args) {
				if (arg != null) {
					String replaceMe = "[/?]";
					sql = sql.replaceFirst(replaceMe, arg.toString());
				}
			}
		}
		if (sql.contains("?")) {
			throw new DataAccessException(
					"Not all Query parameters have been set, please set all values for query to execute");
		}

		return sql;
	}

}
