package com.perfectworldprogramming.mobile.orm.test.interfaces;


import com.perfectworldprogramming.mobile.orm.CursorAdapter;
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

    @Override
    public Address extractData(Cursor cursor) {
        Address address = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                CursorAdapter adapter = new CursorAdapter();
                address = adapter.adaptCurrentFromCursor(cursor, Address.class);
                Person person = adapter.adaptFromCursor(cursor, Person.class);
                address.setPerson(person);
            }
        }

        return address;
    }
}
