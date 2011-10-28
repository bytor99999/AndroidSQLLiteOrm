package com.perfectworldprogramming.mobile.orm.test.interfaces;

import java.util.ArrayList;
import java.util.List;


import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

import android.test.ActivityInstrumentationTestCase2;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 10:50 AM
 */
// TODO need negative path tests
public class CursorExtractorTests extends ActivityInstrumentationTestCase2<Main> {

    AndroidSQLiteTemplate template;
    List<Person> samplePeople = new ArrayList<Person>();
    List<Address> sampleAddress = new ArrayList<Address>();
    DBHelper helper;
    
    public CursorExtractorTests() {
    	super("org.springframework.mobile.orm.test", Main.class);
    }

    @SuppressWarnings("unchecked")
	public void setUp() {
    	try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		helper = new DBHelper(this.getInstrumentation().getContext(), new Class[]{Person.class, Address.class, Account.class}, "ormtest", 3);
        template = new AndroidSQLiteTemplate(helper.getSqlLiteDatabase());
        SampleDataHelper.addDataToDatabase(template);
    }

    public void testSuccessfulPersonExtractor() {
        String sql = "SELECT * from PERSON p, ADDRESS a where a.PERSON_ID = p.PERSON_ID and p.FIRST_NAME = '?'";
        Person person = template.queryForObject(sql, new PersonCursorExtractor(), "John");
        assertNotNull(person);
        assertEquals(new Integer(42), person.getAge());
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals(new Double("5.1d"), person.getHeight());
        List<Address> addresses = person.getAddresses();
        assertNotNull(addresses);
        assertEquals(2, addresses.size());
    }

    public void testSuccessfulAddressExtractor() {
        String sql = "SELECT * from ADDRESS a, PERSON p where a.PERSON_ID = p.PERSON_ID and a.ZIP_CODE='?'";
        Address address = template.queryForObject(sql, new AddressCursorExtractor(), "12345");
        assertNotNull(address);
        assertEquals("Philadelphia", address.getCity());
        assertEquals("PA", address.getState());
        assertEquals("123 Broad Street", address.getStreet());
        assertEquals("12345", address.getZipCode());
        Person person = address.getPerson();
        assertNotNull(person);
        assertEquals(new Integer(42), person.getAge());
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals(new Double("5.1d"), person.getHeight());
    }
}
