package com.perfectworldprogramming.mobile.orm.test.interfaces;


import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.database.Cursor;

/**
 * User: Mark Spritzler
 * Date: 4/7/11
 * Time: 11:47 AM
 */
public class AddressCursorExtractor implements CursorExtractor<Address> {

    private static final String PERSON_ID = "PERSON_ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String HEIGHT = "HEIGHT";
    private static final String AGE = "AGE";
    private static final String ADDRESS_ID = "ADDRESS_ID";
    private static final String STREET = "STREET";
    private static final String CITY = "CITY";
    private static final String STATE = "STATE";
    private static final String ZIP_CODE = "ZIP_CODE";

    @Override
    public Address extractData(Cursor cursor) {
        Address address = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                address = new Address(cursor.getLong(cursor.getColumnIndex(ADDRESS_ID)));
                address.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
                address.setState(cursor.getString(cursor.getColumnIndex(STATE)));
                address.setStreet(cursor.getString(cursor.getColumnIndex(STREET)));
                address.setZipCode(cursor.getString(cursor.getColumnIndex(ZIP_CODE)));
                Person person = new Person(cursor.getLong(cursor.getColumnIndex(PERSON_ID)));
                person.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
                person.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
                person.setHeight(cursor.getDouble(cursor.getColumnIndex(HEIGHT)));
                person.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                address.setPerson(person);
            }
        }

        return address;
    }
}
