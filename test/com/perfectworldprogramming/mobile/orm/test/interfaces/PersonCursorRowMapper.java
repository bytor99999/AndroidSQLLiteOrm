package com.perfectworldprogramming.mobile.orm.test.interfaces;


import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.database.Cursor;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 10:50 AM
 */
public class PersonCursorRowMapper implements CursorRowMapper<Person> {

    private static final String ID = "PERSON_ID";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String HEIGHT = "HEIGHT";
    private static final String AGE = "AGE";
    
    @Override
    public Person mapRow(Cursor cursor, int rowNum) {
        Person person = new Person(cursor.getLong(cursor.getColumnIndex(ID)));
        person.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
        person.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
        person.setHeight(cursor.getDouble(cursor.getColumnIndex(HEIGHT)));
        person.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
        return person;
    }
}
