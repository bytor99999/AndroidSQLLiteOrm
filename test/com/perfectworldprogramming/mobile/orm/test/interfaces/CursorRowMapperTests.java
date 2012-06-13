package com.perfectworldprogramming.mobile.orm.test.interfaces;

import java.util.ArrayList;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;

import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.exception.ExtraResultsException;
import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

/**
 * User: Mark Spritzler Date: 4/6/11 Time: 10:50 AM
 */
public class CursorRowMapperTests extends ActivityInstrumentationTestCase2<Main>
{

    public CursorRowMapperTests()
    {
        super("org.springframework.mobile.orm.test", Main.class);
    }

    AndroidSQLiteTemplate template;
    DBHelper helper;
    List<Person> samplePeople = new ArrayList<Person>();
    List<Address> sampleAddress = new ArrayList<Address>();

    @SuppressWarnings("unchecked")
    public void setUp()
    {
        helper = new DBHelper(this.getInstrumentation().getContext(), new Class[] { Person.class, Address.class, Account.class }, "ormtest", 3);

        template = new AndroidSQLiteTemplate(helper.getSqlLiteDatabase());
        SampleDataHelper.addDataToDatabase(template);
    }

    @Override
    protected void tearDown() throws Exception
    {
        helper.cleanup();
    }

    // Address tests first
    public void testSuccessfulAddressMapperTest()
    {
        List<Address> addresses = template.query("Select * from ADDRESS", new AddressCursorRowMapper());
        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        for (Address address : sampleAddress)
        {
            assertTrue(addresses.contains(address));
        }
    }

    public void testAddressQueryWithNoResultsTest()
    {
        List<Address> addresses = template.query("Select * from ADDRESS where 1=2", new AddressCursorRowMapper());
        assertNotNull(addresses);
        assertEquals(0, addresses.size());
    }

    public void testAddressSingleObjectSuccessTest()
    {
        Address address = template.queryForObject("Select * from ADDRESS where ZIP_CODE='?'", new AddressCursorRowMapper(), "12345");
        assertNotNull(address);
        assertEquals("Philadelphia", address.getCity());
        assertEquals("PA", address.getState());
        assertEquals("123 Broad Street", address.getStreet());
        assertEquals("12345", address.getZipCode());
    }

    // @Test(expected = ExtraResultsException.class)
    public void testAddressSingleObjectReturnsNoResultsTest()
    {
        try
        {
            template.queryForObject("Select * from ADDRESS where ZIP_CODE='?'", new AddressCursorRowMapper(), "98765");
            fail("This test should have thrown an ExtraResultsException stating that 0 rows were returned when expecting 1");
        }
        catch (ExtraResultsException ere)
        {
            String numberOfResults = "0";
            assertEquals("Expected one row returned by query but received " + numberOfResults, ere.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Should have thrown ExtraResultsException, not " + e.getClass().getName());
        }
    }

    // @Test(expected = ExtraResultsException.class)
    public void testAddressSingleObjectReturnsTwoRowsTest()
    {
        try
        {
            template.queryForObject("Select * from ADDRESS", new AddressCursorRowMapper());
        }
        catch (ExtraResultsException ere)
        {
            String numberOfResults = "2";
            assertEquals("Expected one row returned by query but received " + numberOfResults, ere.getMessage());
        }
    }

    // Person tests second
    public void testSuccessfulPersonMapperTest()
    {
        List<Person> persons = template.query("Select * from PERSON", new PersonCursorRowMapper());
        assertNotNull(persons);
        assertEquals(2, persons.size());
        for (Person person : samplePeople)
        {
            assertTrue(persons.contains(person));
        }
    }

    public void testPersonQueryWithNoResultsTest()
    {
        List<Person> persons = template.query("Select * from ADDRESS where 1=2", new PersonCursorRowMapper());
        assertNotNull(persons);
        assertEquals(0, persons.size());
    }

    public void testPersonSingleObjectSuccessTest()
    {
        Person person = template.queryForObject("Select * from PERSON where FIRST_NAME='?'", new PersonCursorRowMapper(), "John");
        assertNotNull(person);
        assertEquals(Integer.valueOf(42), person.getAge());
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals(Double.valueOf("5.1d"), person.getHeight());
    }

    // @Test(expected = ExtraResultsException.class)
    public void testPersonSingleObjectReturnsNoResultsTest()
    {
        try
        {
            template.queryForObject("Select * from PERSON where FIRST_NAME='?' and AGE=?", new PersonCursorRowMapper(), "George", 37);
        }
        catch (ExtraResultsException ere)
        {
            String numberOfResults = "0";
            assertEquals("Expected one row returned by query but received " + numberOfResults, ere.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Should have thrown ExtraResultsException, not " + e.getClass().getName());
        }
    }

    // @Test(expected = ExtraResultsException.class)
    public void testPersonSingleObjectReturnsTwoRowsTest()
    {
        try
        {
            template.queryForObject("Select * from PERSON", new PersonCursorRowMapper());
        }
        catch (ExtraResultsException ere)
        {
            String numberOfResults = "2";
            assertEquals("Expected one row returned by query but received " + numberOfResults, ere.getMessage());
        }
        catch (Exception e)
        {
            fail("Should have thrown ExtraResultsException, not " + e.getClass().getName());
        }
    }
}
