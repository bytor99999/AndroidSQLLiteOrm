package com.perfectworldprogramming.mobile.orm.test.interfaces;


import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.CursorAdapter;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 10:55 AM
 */
public class AddressCursorRowMapper implements CursorRowMapper<Address> {
    
    @Override
    public Address mapRow(Cursor cursor, int rowNum) {
        return new CursorAdapter().adaptCurrentFromCursor(cursor, Address.class);
    }
}
