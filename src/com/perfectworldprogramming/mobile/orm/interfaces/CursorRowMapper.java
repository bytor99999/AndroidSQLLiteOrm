package com.perfectworldprogramming.mobile.orm.interfaces;

import android.database.Cursor;

/**
 * User: Mark Spritzler
 * Date: 3/19/11
 * Time: 2:35 PM
 */
public interface CursorRowMapper<T> {
    
    T mapRow(Cursor cursor, int rowNum);
}
