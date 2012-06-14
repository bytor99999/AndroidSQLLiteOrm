package com.perfectworldprogramming.mobile.orm.test.oql;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.oql.AndroidOqlTemplate;
import com.perfectworldprogramming.mobile.orm.oql.OqlParameter;
import com.perfectworldprogramming.mobile.orm.test.AndroidSQLiteTemplateTests;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;
import com.perfectworldprogramming.mobile.orm.test.interfaces.SampleDataHelper;

/**
 * Essentially a copy of {@link AndroidSQLiteTemplateTests} using {@link QueryParameter}s
 * so that database conversion of values is hidden from domain layer, excluding the column name,
 * which is already declared in the domain classes.
 * @author David O'Meara <david.omeara@gmail.com>
 * @since 10/06/2012
 *
 */
public class OqlQueryParameterTest extends ActivityInstrumentationTestCase2<Main> {

    AndroidOqlTemplate template;
    DBHelper helper;
    List<Account> sampleAccounts;

    SQLiteDatabase dataBase;

    public OqlQueryParameterTest() {
        super("org.springframework.mobile.orm.test", Main.class);
    }

    @SuppressWarnings("unchecked")
    public void setUp() {
        helper = new DBHelper(this.getInstrumentation().getContext(), new Class[]{Person.class, Address.class, Account.class}, "ormtest", 3);
        dataBase = helper.getSqlLiteDatabase();
        template = new AndroidOqlTemplate(helper.getSqlLiteDatabase());
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
        OqlParameter param = new OqlParameter(Person.class, "id", 1L);
        String oql = "SELECT * from Person where [id] = ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByString()
    {
        OqlParameter param = new OqlParameter(Person.class, "firstName", "John");
        String oql = "SELECT * from Person where [firstName] = '?'";
        
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByInteger()
    {
        OqlParameter param = new OqlParameter(Person.class, "age", 42);
        
        String oql = "SELECT * from Person where [age] = ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
        assertEquals("Age", Integer.valueOf(42), person.getAge());

        param = new OqlParameter(Person.class, "jacketSize", 43);
        oql = "SELECT * from Person where [jacketSize] = ?";
        person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
        assertEquals("Age", Integer.valueOf(42), person.getAge());
        assertEquals("Jacket size", 43, person.getJacketSize());
    }

    public void testSqlQueryByLong()
    {
        OqlParameter param = new OqlParameter(Person.class, "dependants", 2D);
        String oql = "SELECT * from Person where [dependants] = ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        param = new OqlParameter(Person.class, "pets", 1D);
        oql = "SELECT * from Person where [pets] = ?";
        person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByBoolean()
    {
        OqlParameter param = new OqlParameter(Person.class, "staff", true);
        String oql = "SELECT * from Person where "+Person.COL_STAFF+" = ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        param = new OqlParameter(Person.class, "female", false);
        oql = "SELECT * from Person where [female] = ?";
        person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByFloat()
    {
        OqlParameter param = new OqlParameter(Person.class, "weight", 200F);
        String oql = "SELECT * from Person where [weight] < ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        param = new OqlParameter(Person.class, "shoeSize", 10F);
        oql = "SELECT * from Person where [shoeSize] < ?";
        person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByDouble()
    {
        OqlParameter param = new OqlParameter(Person.class, "height", 6D);
        String oql = "SELECT * from Person where [height] < ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());

        param = new OqlParameter(Person.class, "wealth", 200000D);
        oql = "SELECT * from Person where [wealth] > ?";
        person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testSqlQueryByDate()
    {
        OqlParameter param = new OqlParameter(Person.class, "startDate", new java.sql.Date(2012-1900, 2, 1));
        String oql = "SELECT * from Person where [startDate] < ?";
        Person person = template.queryForObject(oql, Person.class, param);
        assertNotNull(person);
        assertEquals("ID", Long.valueOf(1), person.getId());
        assertEquals("First name", "John", person.getFirstName());
    }
}
