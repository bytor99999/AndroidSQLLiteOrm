package com.perfectworldprogramming.mobile.orm.test.interfaces;


import android.database.Cursor;

import com.perfectworldprogramming.mobile.orm.CursorAdapter;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 10:50 AM
 */
public class PersonCursorRowMapper implements CursorRowMapper<Person> {
    @Override
    public Person mapRow(Cursor cursor, int rowNum) {
        return new CursorAdapter().adaptCurrentFromCursor(cursor, Person.class);
    }
}
