package com.perfectworldprogramming.mobile.orm.test.reflection;

import java.lang.reflect.Field;


import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.reflection.DomainClassAnalyzer;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;

/**
 * User: Mark Spritzler
 * Date: 3/14/11
 * Time: 5:57 PM
 */
public class DomainClassAnalyzerTests extends ActivityInstrumentationTestCase2<Main> {
    DomainClassAnalyzer domainClassAnalyzer = new DomainClassAnalyzer();

    public DomainClassAnalyzerTests() {
    	super("org.springframework.mobile.orm.test", Main.class);
    }
    
    public void setUp() {
    	try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void testGetPrimaryKeyFieldTest() {
        Field field = domainClassAnalyzer.getPrimaryKeyField(Person.class);
        assertNotNull("primary key field should not be null", field);
        assertEquals("", Long.class, field.getType());
    }

    public void testGetPrimaryKeyNameTest() {
        String primaryKeyName = domainClassAnalyzer.getPrimaryKeyFieldName(Person.class);
        assertEquals("primary key name should be PERSON_ID", "PERSON_ID", primaryKeyName);
        primaryKeyName = domainClassAnalyzer.getPrimaryKeyFieldName(Address.class);
        assertEquals("primary key name should be ADDRESS_ID", "ADDRESS_ID", primaryKeyName);
    }

    public void testGetPrimaryKeyTest() {
        PrimaryKey primaryKey = domainClassAnalyzer.getPrimaryKey(Person.class);
        assertNotNull("primary key annotation should be found", primaryKey);
    }

    public void testGetForeignKeyFieldsTest() {
        Field[] personForeignKeyFields = domainClassAnalyzer.getForeignKeyFields(Person.class);
        assertEquals("", 0, personForeignKeyFields.length);
        Field[] addressForeignKeyFields = domainClassAnalyzer.getForeignKeyFields(Address.class);
        assertEquals("", 1, addressForeignKeyFields.length);
    }

    public void testGetIdFromObjectTest() {
        Address address = getTestAddress();
        Long id = domainClassAnalyzer.getIdFromObject(address);
        assertNotNull("id should not be null", id);
        assertEquals("id for address should be 15", new Long(15), id);
        Person person = getTestPerson();
        id = domainClassAnalyzer.getIdFromObject(person);
        assertNotNull("id should not be null", id);
        assertEquals("id for person should be 100", new Long(100), id);
    }

    public void testCreateContentValuesTest() {
        ContentValues personValues = domainClassAnalyzer.createContentValues(getTestPerson());
        assertNotNull(personValues);
        ContentValues addressValues = domainClassAnalyzer.createContentValues(getTestAddress());
        assertNotNull(addressValues);
    }

    private Person getTestPerson() {
        Person person = new Person();
        person.setAge(42);
        person.setFirstName("George");
        person.setHeight(5.10);
        person.setLastName("Jetson");
        person.addAddress(getTestAddressForPerson(person));
        try {
            Field field = person.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(person, new Long(100));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return person;
    }

    private Address getTestAddress() {
        Address address = new Address();
        address.setCity("Long Beach");
        Person person = getTestPerson();
        person.addAddress(address);
        address.setPerson(person);
        address.setState("CA");
        address.setStreet("123 Elm Street");
        address.setZipCode("90808");
        try {
            Field field = address.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(address, 15l);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return address;
    }

    private Address getTestAddressForPerson(Person person) {
        Address address = new Address();
        address.setCity("Long Beach");
        address.setPerson(person);
        address.setState("CA");
        address.setStreet("123 Elm Street");
        address.setZipCode("90808");
        try {
            Field field = address.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(address, 16l);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return address;
    }
}
