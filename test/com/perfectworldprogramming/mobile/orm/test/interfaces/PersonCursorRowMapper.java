package com.perfectworldprogramming.mobile.orm.test.interfaces;


import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.database.Cursor;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 10:50 AM
 */
@Deprecated
public class PersonCursorRowMapper implements CursorRowMapper<Person> {

    private static final String ID = Person.PK_PERSON;
    private static final String FIRST_NAME = Person.COL_FIRST_NAME;
    private static final String LAST_NAME = "LAST_NAME";
    private static final String HEIGHT = "HEIGHT";
    private static final String AGE = "AGE";
    private static final String STAFF = "STAFF";
    
    @Override
    public Person mapRow(Cursor cursor, int rowNum) {
        Person person = new Person(cursor.getLong(cursor.getColumnIndex(ID)));
        person.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
        person.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
        person.setHeight(cursor.getDouble(cursor.getColumnIndex(HEIGHT)));
        person.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
        person.setStaff(cursor.getInt(cursor.getColumnIndex(STAFF))==1);
        return person;
    }
}
