package com.perfectworldprogramming.mobile.orm.test.interfaces;

import java.util.List;

import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.CursorAdapter;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 4:21 PM
 */
public class PersonCursorExtractor implements CursorExtractor<Person> {

    @Override
    public Person extractData(Cursor cursor) {
        Person person = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                CursorAdapter adapter = new CursorAdapter();
                person = adapter.adaptCurrentFromCursor(cursor, Person.class);
                //cursor.getLong(cursor.getColumnIndex(PERSON_ID)));
//                person.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
//                person.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
//                person.setHeight(cursor.getDouble(cursor.getColumnIndex(HEIGHT)));
//                person.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                List<Address> addresses = adapter.adaptListFromCursor(cursor, Address.class);
//                do {
//                    Address address = new Address(cursor.getLong(cursor.getColumnIndex(ADDRESS_ID)));
//                    address.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
//                    address.setState(cursor.getString(cursor.getColumnIndex(STATE)));
//                    address.setStreet(cursor.getString(cursor.getColumnIndex(STREET)));
//                    address.setZipCode(cursor.getString(cursor.getColumnIndex(ZIP_CODE)));
//                    person.addAddress(address);
//                } while (cursor.moveToNext());
                person.setAddresses(addresses);
            }
        }
        return person;
    }
}
