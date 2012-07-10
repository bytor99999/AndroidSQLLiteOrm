package com.perfectworldprogramming.mobile.orm.test.reflection;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.test.AndroidSQLiteTemplateTests;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;
import com.perfectworldprogramming.mobile.orm.test.interfaces.SampleDataHelper;

/**
 * Essentially a copy of {@link AndroidSQLiteTemplateTests} using {@link AndroidSQLiteTemplate.mapQueryParameter()}
 * so that database conversion of values is hidden from domain layer (excluding the column name,
 * which is already declared in the domain classes).
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 10/06/2012
 *
 */
public class QueryParameterTest extends ActivityInstrumentationTestCase2<Main> {

    AndroidSQLiteTemplate template;
    DBHelper helper;
    List<Account> sampleAccounts;

    SQLiteDatabase dataBase;

    public QueryParameterTest() {
        super("org.springframework.mobile.orm.test", Main.class);
    }

    @SuppressWarnings("unchecked")
    public void setUp() {
        helper = new DBHelper(this.getInstrumentation().getContext(), new Class[]{Person.class, Address.class, Account.class}, "ormtest", 3);
        dataBase = helper.getSqlLiteDatabase();
        template = new AndroidSQLiteTemplate(helper.getSqlLiteDatabase());
        SampleDataHelper.addDataToDatabase(template);
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        helper.cleanup();
    }

    // Currently passes
    public void testSqlQueryByPrimaryKey()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(1L, Person.class, Person.PK_PERSON);
        
        String sql = "SELECT * from Person where "+Person.PK_PERSON+" = ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByString()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter("John", Person.class, Person.COL_FIRST_NAME);
        
        String sql = "SELECT * from Person where "+Person.COL_FIRST_NAME+" = '?'";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByInteger()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(42, Person.class, Person.COL_AGE);
        
        String sql = "SELECT * from Person where "+Person.COL_AGE+" = ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
        assertEquals("Age", Integer.valueOf(42), person.getAge());

        args[0] = template.mapQueryParameter(43, Person.class, Person.COL_JACKET_SIZE);
        sql = "SELECT * from Person where "+Person.COL_JACKET_SIZE+" = ?";
        person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
        assertEquals("Age", Integer.valueOf(42), person.getAge());
        assertEquals("Jacket size", 43, person.getJacketSize());
    }

    public void testSqlQueryByLong()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(2D, Person.class, Person.COL_DEPENDANTS);
        
        String sql = "SELECT * from Person where "+Person.COL_DEPENDANTS+" = ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        args[0] = template.mapQueryParameter(1D, Person.class, Person.COL_PETS);
        sql = "SELECT * from Person where "+Person.COL_PETS+" = ?";
        person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByBoolean()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(true, Person.class, Person.COL_STAFF);
        
        String sql = "SELECT * from Person where "+Person.COL_STAFF+" = ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        args[0] = template.mapQueryParameter(false, Person.class, Person.COL_FEMALE);
        sql = "SELECT * from Person where "+Person.COL_FEMALE+" = ?";
        person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByFloat()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(200F, Person.class, Person.COL_WEIGHT);
        
        String sql = "SELECT * from Person where "+Person.COL_WEIGHT+" < ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        args[0] = template.mapQueryParameter(10F, Person.class, Person.COL_SHOE_SIZE);
        sql = "SELECT * from Person where "+Person.COL_SHOE_SIZE+" < ?";
        person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByDouble()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(6D, Person.class, Person.COL_HEIGHT);
        
        String sql = "SELECT * from Person where "+Person.COL_HEIGHT+" < ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        args[0] = template.mapQueryParameter(200000D, Person.class, Person.COL_WEALTH);
        sql = "SELECT * from Person where "+Person.COL_WEALTH+" > ?";
        person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByDate()
    {
        Object[] args = new Object[1];
        args[0] = template.mapQueryParameter(new java.sql.Date(2012-1900, 2, 1), Person.class, Person.COL_START_DATE);
        
        String sql = "SELECT * from Person where "+Person.COL_START_DATE+" < ?";
        Person person = template.queryForObject(sql, Person.class, args);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }
}
