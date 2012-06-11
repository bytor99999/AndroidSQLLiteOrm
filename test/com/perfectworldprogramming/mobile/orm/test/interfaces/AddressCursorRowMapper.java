package com.perfectworldprogramming.mobile.orm.test.interfaces;


import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;

import android.database.Cursor;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 10:55 AM
 */
@Deprecated
public class AddressCursorRowMapper implements CursorRowMapper<Address> {

    private static final String ID = "ADDRESS_ID";
    private static final String STREET = "STREET";
    private static final String CITY = "CITY";
    private static final String STATE = "STATE";
    private static final String ZIP_CODE = "ZIP_CODE";
    
    @Override
    public Address mapRow(Cursor cursor, int rowNum) {
        Address address = new Address(cursor.getLong(cursor.getColumnIndex(ID)));
        address.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
        address.setState(cursor.getString(cursor.getColumnIndex(STATE)));
        address.setStreet(cursor.getString(cursor.getColumnIndex(STREET)));
        address.setZipCode(cursor.getString(cursor.getColumnIndex(ZIP_CODE)));
        return address;
    }
}
