package com.perfectworldprogramming.mobile.orm.interfaces;

import android.database.Cursor;

/**
 * Callback interface used by {@link com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate}'s query methods.
 * Implementations of this interface perform the actual work of extracting
 * results from a {@link java.sql.ResultSet}, but don't need to worry
 * about exception handling. {@link java.sql.SQLException SQLExceptions}
 * will be caught and handled by the calling AndroidSQLiteTemplate.
 *
 * <p>This interface is mainly used within the SQLLite framework itself.
 * A {@link CursorRowMapper} is usually a simpler choice for Cursor processing,
 * mapping one result object per row instead of one result object for
 * the entire ResultSet.
 *
 * @author Mark Spritzler
 * @since 3/19/11
 * @see com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate
 * @see CursorRowMapper
 */
public interface CursorExtractor<T> {

    /**
	 * Implementations must implement this method to process the entire Cursor.
	 * @param cursor Cursor to extract data from. Implementations should
	 * not close this: it will be closed by the calling AndroidSQLiteTemplate.
	 * @return an arbitrary result object, or <code>null</code> if none
	 */
	T extractData(Cursor cursor);
}
