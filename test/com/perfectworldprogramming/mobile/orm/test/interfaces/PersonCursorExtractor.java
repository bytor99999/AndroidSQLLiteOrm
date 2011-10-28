package com.perfectworldprogramming.mobile.orm.test.interfaces;

import android.database.Cursor;


import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 4:21 PM
 */
public class PersonCursorExtractor implements CursorExtractor<Person> {

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
    public Person extractData(Cursor cursor) {
        Person person = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                person = new Person(cursor.getLong(cursor.getColumnIndex(PERSON_ID)));
                person.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
                person.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
                person.setHeight(cursor.getDouble(cursor.getColumnIndex(HEIGHT)));
                person.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                do {
                    Address address = new Address(cursor.getLong(cursor.getColumnIndex(ADDRESS_ID)));
                    address.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
                    address.setState(cursor.getString(cursor.getColumnIndex(STATE)));
                    address.setStreet(cursor.getString(cursor.getColumnIndex(STREET)));
                    address.setZipCode(cursor.getString(cursor.getColumnIndex(ZIP_CODE)));
                    person.addAddress(address);
                } while (cursor.moveToNext());
            }
        }
        return person;
    }
}
