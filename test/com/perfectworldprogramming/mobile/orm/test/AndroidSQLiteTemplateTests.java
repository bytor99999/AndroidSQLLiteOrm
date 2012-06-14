package com.perfectworldprogramming.mobile.orm.test;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.exception.EmptySQLStatementException;
import com.perfectworldprogramming.mobile.orm.exception.InvalidCursorException;
import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorExtractor;
import com.perfectworldprogramming.mobile.orm.interfaces.CursorRowMapper;
import com.perfectworldprogramming.mobile.orm.reflection.DomainClassAnalyzer;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.NoPKDomain;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;
import com.perfectworldprogramming.mobile.orm.test.interfaces.AddressCursorExtractor;
import com.perfectworldprogramming.mobile.orm.test.interfaces.AddressCursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.interfaces.PersonCursorExtractor;
import com.perfectworldprogramming.mobile.orm.test.interfaces.PersonCursorRowMapper;
import com.perfectworldprogramming.mobile.orm.test.interfaces.SampleDataHelper;

/**
 * User: Mark Spritzler Date: 4/7/11 Time: 12:06 PM
 */
public class AndroidSQLiteTemplateTests extends
		ActivityInstrumentationTestCase2<Main> {

	public AndroidSQLiteTemplateTests() {
		super("org.springframework.mobile.orm.test", Main.class);
	}

	AndroidSQLiteTemplate template;
	DBHelper helper;
	List<Account> sampleAccounts;

	SQLiteDatabase dataBase;

	@SuppressWarnings("unchecked")
	public void setUp() {
		helper = new DBHelper(this.getInstrumentation().getContext(),
				new Class[] { Person.class, Address.class, Account.class },
				"ormtest.db", 3);
		template = new AndroidSQLiteTemplate(helper.getSqlLiteDatabase());
		SampleDataHelper.addDataToDatabase(template);
		sampleAccounts = SampleDataHelper.getAccounts();
		dataBase = helper.getSqlLiteDatabase();
	}

	@Override
	protected void tearDown() throws Exception {
		if (helper != null) {
			helper.cleanup();
		}
	}

	// Currently passes
	public void testSuccessInsertWithDomainObject() {
		Person person = new Person();
		person.setAge(5);
		person.setFirstName("Ryan");
		person.setLastName("Simpson");
		person.setHeight(2.6);
		long id = template.insert(person);

		// Check out by using the api directly.
		Cursor cursor = dataBase.rawQuery(
				"Select * from Person where PERSON_ID = " + id, null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		assertEquals("Ryan",
				cursor.getString(cursor.getColumnIndex(Person.COL_FIRST_NAME)));
		assertEquals("Simpson",
				cursor.getString(cursor.getColumnIndex("LAST_NAME")));
	}

	// Currently passes but needs more code
	/*
	 * Missing Mandatory/not Nullable Field Field that is set to unique is not
	 * unique (next version, but the code still should work in this scenario and
	 * would throw a DataAccessException)
	 */
	public void testFailureInsertWithDomainObject() {
		// missing mandatory field
		Person person = new Person();
		person.setAge(5);
		person.setLastName("Simpson");
		person.setHeight(2.6);
		try {
			template.insert(person);
			fail("Exception should be thrown because a mandatory field is not set");
		} catch (DataAccessException dae) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite: "
					+ e.getClass() + ": " + e.getMessage());
		}
	}

	// Currently Passes
	public void testSuccessInsertSQL() {
		String sql = "INSERT INTO Person (FIRST_NAME, LAST_NAME, AGE) VALUES ('?', '?', ?)";
		Object[] args = { "Bugs", "Bunny", 10 };
		long id = template.insert(sql, args);

		Cursor cursor = dataBase.rawQuery(
				"Select * from Person where PERSON_ID = " + id, null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		assertEquals("Bugs",
				cursor.getString(cursor.getColumnIndex(Person.COL_FIRST_NAME)));
		assertEquals("Bunny",
				cursor.getString(cursor.getColumnIndex("LAST_NAME")));
		assertEquals(10, cursor.getInt(cursor.getColumnIndex("AGE")));
	}

	// Currently Passes
	/*
	 * Invalid Insert statement string using FIELDS Using an update statement
	 * instead of an insert
	 */
	public void testFailureInsertSQL() {
		String sql = "INSERT INTO Person FIELDS (FIRST_NAME, LAST_NAME, AGE) VALUES ('?', '?', ?)";
		Object[] args = { "Bugs", "Bunny", 10 };
		try {
			template.insert(sql, args);
			fail("Exception should be thrown because a insert statement is invalid");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite");
		}

		sql = "UPDATE Person (FIRST_NAME, LAST_NAME, AGE) VALUES ('?', '?', ?)";
		try {
			template.insert(sql, args);
			fail("Exception should be thrown because it is an update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite");
		}
	}

	// Currently passes
	public void testSuccessUpdateWithDomainObject() {
		// Take the last account in collection and make changes
		// First just a Real field
		Account account = sampleAccounts.get(sampleAccounts.size() - 1);
		account.setAmount(42.00);
		long numberOfAccountsUpdated = template.update(account);

		assertEquals(1, numberOfAccountsUpdated);
		assertAccount(account, "Select * from Account where YEAR_STARTED=2011");

		// Now an Integer field
		account.setYearAccountOpened(1900);
		numberOfAccountsUpdated = template.update(account);
		assertEquals(1, numberOfAccountsUpdated);
		assertAccount(account, "Select * from Account where YEAR_STARTED=1900");

		// Now a Text field
		account.setAccountType("Invalid");
		numberOfAccountsUpdated = template.update(account);
		assertEquals(1, numberOfAccountsUpdated);
		assertAccount(account, "Select * from Account where YEAR_STARTED=1900");

		// Now all three field types in one update
		account.setAccountType("Business Pro");
		account.setYearAccountOpened(2011);
		account.setAmount(4200.00);
		numberOfAccountsUpdated = template.update(account);
		assertEquals(1, numberOfAccountsUpdated);
		assertAccount(account, "Select * from Account where YEAR_STARTED=2011");

	}

	// Currently Passes
	public void testFailureUpdateWithDomainObject() {
		// No Id set in domain object
		Account account = new Account();
		try {
			template.update(account);
			fail("Exception should be thrown because id is not set");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			Log.i("ORM", Log.getStackTraceString(e));
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// Use an object that is not mapped
		Long longObject = Long.valueOf(5);
		try {
			template.update(longObject);
			fail("Exception should be thrown because Long is not a domain object");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// Object is mapped, but not a table in the database
		NoPKDomain noPK = new NoPKDomain();
		try {
			template.update(noPK);
			fail("Exception should be thrown because there isn't a table for this domain mapped object");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

	}

	// Currently passes
	public void testSuccessUpdateSQL() {
		// Update statement to one row
		String sql = "UPDATE Account set AMOUNT = ? where ACCOUNT_TYPE = '?'";
		Object[] args = { Double.valueOf(68.00), "Personal" };
		template.update(sql, args);

		Cursor cursor = dataBase.rawQuery(
				"Select Amount from Account where ACCOUNT_TYPE='Personal'",
				null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Double results = cursor.getDouble(0);
		assertEquals(Double.valueOf(68.00), results);

		// Update statement to more than one row
		sql = "UPDATE Account set AMOUNT = ? where ACCOUNT_TYPE = '?'";
		args[0] = Double.valueOf(67.00);
		args[1] = "Business";
		template.update(sql, args);
		sql = "Select " + Account.COL_AMOUNT + " from Account where "
				+ Account.COL_ACCOUNT_TYPE + "='Business'";
		cursor = dataBase.rawQuery(sql, null);
		assertNotNull(cursor);
		assertEquals(3, cursor.getCount());
		cursor.moveToFirst();
		results = cursor.getDouble(0);
		assertEquals(Double.valueOf(67.00), results);

		// a delete statement
		// This will work, because you can pass a delete statement to update
		sql = "Delete from Account where " + Account.PK_ACCOUNT_ID + " = ?";
		template.update(sql, new Object[] { 1 });

	}

	/*
	 * Currently passes Null sql, invalid update table doesn't exist set field
	 * doesn't exist set field to wrong type an insert statement, a select
	 * statement
	 */
	public void testFailureUpdateSQL() {
		String sql = "";
		Object[] nullArgs = {};

		// empty sql
		try {
			template.update(sql, nullArgs);
			fail("Exception should be thrown because there isn't an update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// table doesn't exist
		sql = "UPDATE NonExistingTable set AMOUNT = ? where ACCOUNT_TYPE = '?'";
		Object[] validArgs = { Double.valueOf(67.00), "Business" };
		try {
			template.update(sql, validArgs);
			fail("Exception should be thrown because the table doesn't exist in update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// set field doesn't exist
		sql = "UPDATE Account set Non_Field = ? where ACCOUNT_TYPE = '?'";
		try {
			template.update(sql, validArgs);
			fail("Exception should be thrown because the field doesn't exist update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// set field to wrong type
		sql = "UPDATE Account set AMOUNT = ? where ACCOUNT_TYPE = '?'";
		Object[] invalidArgs = { "Hello", "Business" };
		try {
			template.update(sql, invalidArgs);
			fail("Exception should be thrown because the field value is the wrong type update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// an insert statement,
		sql = "INSERT INTO Person FIELDS (FIRST_NAME, LAST_NAME, AGE) VALUES ('?', '?', ?)";
		Object[] insertArgs = { "Bugs", "Bunny", 10 };
		try {
			template.update(sql, insertArgs);
			fail("Exception should be thrown because this is an insert statement not an update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// a select statement
		sql = "Select * from Person";
		try {
			template.update(sql, new Object[] {});
			fail("Exception should be thrown because this is a select statement not an update statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

	}

	// Currently passes
	public void testSuccessDeleteWithDomainObject() {
		Account account = sampleAccounts.get(5);
		long results = template.delete(account);
		assertEquals(1, results);
	}

	/*
	 * Currently passes null domain object domain object with invalid id domain
	 * object without an id
	 */
	public void testFailureDeleteWithDomainObject() {
		Account account = null;
		try {
			template.delete(account);
			fail("Exception should be thrown because the domain object is null");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		account = new Account();
		DomainClassAnalyzer analyzer = new DomainClassAnalyzer();
		analyzer.setIdToNewObject(account, 42);
		try {
			template.delete(account);
			fail("Exception should be thrown because this is an invalid id");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		account = new Account();
		try {
			template.delete(account);
			fail("Exception should be thrown because the id in the domain object is null");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}
	}

	// Currently Passes
	public void testSuccessDeleteWithTableNameAndIdsToDelete() {
		String table = "Account";
		String keyColumnName = "ACCOUNT_ID";
		String keyValue = "1";
		long results = template.delete(table, keyColumnName, keyValue);
		assertEquals(1, results);

		keyValue = "2";
		results = template.delete(table, keyColumnName, keyValue);
		assertEquals(1, results);
	}

	/*
	 * Currently Passes empty strings and string array null table null
	 * keyColumnName null keyValues Wrong table name Wrong key column name Wrong
	 * id doesn't exist
	 */
	public void testFailureDeleteWithTableNameAndIdsToDelete() {
		// empty strings and string array
		String table = "";
		String keyColumnName = "";
		String keyValue = "";
		try {
			template.delete(table, keyColumnName, keyValue);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// null table
		table = null;
		keyColumnName = "ACCOUNT_ID";
		keyValue = "1";
		try {
			template.delete(table, keyColumnName, keyValue);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// null keyColumnName
		table = "Account";
		keyColumnName = null;
		try {
			template.delete(table, keyColumnName, keyValue);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// null keyValues
		keyColumnName = "ACCOUNT_ID";
		keyValue = null;
		try {
			template.delete(table, keyColumnName, keyValue);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// Fix the null from previous test
		keyValue = "1";

		// Wrong table name
		table = "NonExistingTable";
		try {
			template.delete(table, keyColumnName, keyValue);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// Wrong key column name
		table = "Account";
		keyColumnName = "BAD_ID";
		try {
			template.delete(table, keyColumnName, keyValue);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// Wrong id doesn't exist
		keyColumnName = "ACCOUNT_ID";
		keyValue = "42";
		long results = template.delete(table, keyColumnName, keyValue);
		assertEquals(0, results);
	}

	// Currently Passes
	public void testSuccessDeleteWithSQL() {
		String sql = "Delete from Account where ACCOUNT_TYPE = '?'";
		Object[] args = { "Personal" };
		template.delete(sql, args);

		sql = "Delete from Account where ACCOUNT_TYPE = '?'";
		args[0] = "Business";
		template.delete(sql, args);
	}

	// Currently Passes
	public void testFailureDeleteWithSQL() {
		String sql = "";
		Object[] nullArgs = {};

		// empty sql
		try {
			template.delete(sql, nullArgs);
			fail("Exception should be thrown because there isn't a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// table doesn't exist
		sql = "Delete from NonExistingTable where ACCOUNT_TYPE = '?'";
		Object[] validArgs = { "Business" };
		try {
			template.delete(sql, validArgs);
			fail("Exception should be thrown because the table doesn't exist in delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// an insert statement,
		sql = "INSERT INTO Person FIELDS (FIRST_NAME, LAST_NAME, AGE) VALUES ('?', '?', ?)";
		Object[] insertArgs = { "Bugs", "Bunny", 10 };
		try {
			template.delete(sql, insertArgs);
			fail("Exception should be thrown because this is an insert statement not a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// a select statement
		sql = "Select * from Person";
		try {
			template.delete(sql, new Object[] {});
			fail("Exception should be thrown because this is a select statement not a delete statement");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}
	}

	// Currently passes
	public void testSuccessQueryForInt() {
		String sql = "Select count(*) from Person";
		String args = "";
		Integer results = template.queryForInt(sql, args);

		assertNotNull(results);
		Cursor cursor = dataBase.rawQuery("Select count(*) from Person", null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Integer realCount = cursor.getInt(0);
		assertEquals(realCount, results);
	}

	// Currently Passes
	public void testFailureQueryForInt() {
		String sql = "Select FIRST_NAME from Person Where FIRST_NAME='?'";
		String args = "George";
		try {
			template.queryForInt(sql, args);
			fail("DataAccessException should be thrown because query returns zero rows");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		// returns one row, but since it is a String the int is set to 0;
		args = "John";
		int zero = template.queryForInt(sql, args);
		assertEquals(0, zero);

	}

	// Currently passes
	public void testSuccessQueryForLong() {
		String sql = "Select count(*) from Person";
		String args = "";
		Long results = template.queryForLong(sql, args);

		assertNotNull(results);
		Cursor cursor = dataBase.rawQuery("Select count(*) from Person", null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Long realCount = cursor.getLong(0);
		assertEquals(realCount, results);
	}

	// Currently Passes
	public void testFailureQueryForLong() {
		String sql = "Select FIRST_NAME from Person Where FIRST_NAME='?'";
		String args = "George";
		try {
			template.queryForLong(sql, args);
			fail("DataAccessException should be thrown because query returns zero rows");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}
		args = "John";
		long zero = template.queryForLong(sql, args);
		assertEquals(0, zero);

	}

	// Currently passes
	public void testSuccessQueryForString() {
		String sql = "Select FIRST_NAME from Person Where FIRST_NAME='?'";
		String args = "John";
		String results = template.queryForString(sql, args);

		assertNotNull(results);
		assertEquals("John", results);

		// Still will be successful and convert the int field to a String
		sql = "Select AGE from Person Where FIRST_NAME='?'";
		results = template.queryForString(sql, args);
		assertNotNull(results);
		assertEquals("42", results);

		// Still will be successful with more than one row but return just the
		// first row's value
		sql = "Select age from Person";
		args = null;
		results = template.queryForString(sql, args);
		assertNotNull(results);
		assertEquals("42", results);

	}

	// Currently passes
	public void testFailureQueryForString() {
		String sql = "Select AGE from Person Where FIRST_NAME='?'";
		String args = "George";
		try {
			template.queryForString(sql, args);
			fail("DataAccessException should be thrown because it is returning 0 rows");
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}
	}

	// Currently passes
	public void testSuccessQueryForObjectWithDomainClass() {
		final String sql = "Select * from Person where FIRST_NAME='?'";
		Class<Person> clazz = Person.class;
		String args = "John";
		final Person john = template.queryForObject(sql, clazz, args);
		assertJohnThePerson(john);

		// Still successful because SQLite returns the first row even if the
		// query returns more than one row
		final String sql_star = "Select * from Person";
		args = null;
		Person person = template.queryForObject(sql_star, clazz, args);
		assertJohnThePerson(person);

		args = "Jane";
		final Person jane = template.queryForObject(sql, clazz, args);
		assertJaneThePerson(jane);
	}

	// Currently passes.
	public void testFailureQueryForObjectWithDomainClass() {
		String sql = "Select * from Person where FIRST_NAME='?'";
		Class<Person> clazz = Person.class;
		String args = "George";
		try {
			Object results = template.queryForObject(sql, clazz, args);
			fail("DataAccessException should be thrown because results returned 0 rows but returns: "
					+ results);
		} catch (DataAccessException de) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}
	}

	// Currently passes
	public void testSuccessQueryForObjectWithCursorRowMapper() {
		String sql = "Select * from ADDRESS where ZIP_CODE='?'";
		AddressCursorRowMapper addressCursorRowMapper = new AddressCursorRowMapper();
		Address address = template.queryForObject(sql, addressCursorRowMapper,
				"12345");

		assertNotNull(address);
		assertEquals("Philadelphia", address.getCity());
		assertEquals("PA", address.getState());
		assertEquals("123 Broad Street", address.getStreet());
		assertEquals("12345", address.getZipCode());
	}

	// Currently passes
	public void testFailuresQueryForObjectWithCursorRowMapper() {
		String sql = "";
		CursorRowMapper<Address> cursorRowMapper = null;
		String args = "";
		try {
			template.queryForObject(sql, cursorRowMapper, args);
			fail("Should throw an IllegalArgumentException because the cursorRowMapper is null");
		} catch (IllegalArgumentException ie) {
		} catch (Exception e) {
			fail("Only an IllegalArgumentException should be thrown");
		}

		cursorRowMapper = new AddressCursorRowMapper();
		try {
			template.queryForObject(sql, cursorRowMapper, args);
			fail("Should throw an EmptySQLStatementException because the cursorRowMapper is null");
		} catch (EmptySQLStatementException ie) {
		} catch (Exception e) {
			fail("Only an EmptySQLStatementException should be thrown "
					+ e.getMessage());
		}

		sql = "Select * from Person Where FIRST_NAME = '?'";
		args = "John";
		try {
			template.queryForObject(sql, cursorRowMapper, args);
			fail("Should throw an InvalidCursorRowMapperException because the cursorRowMapper is of the wrong type. it is an Address Row mapper but the query is from Person");
		} catch (InvalidCursorException ie) {
		} catch (Exception e) {
			fail("Only an InvalidCursorException should be thrown: "
					+ e.getMessage());
		}
	}

	// Currently passes
	public void testSuccessQueryForObjectWithCursorExtractor() {
		String sql = "SELECT * from PERSON p, ADDRESS a where a.PERSON_ID = p.PERSON_ID and p.FIRST_NAME = '?'";
		Person person = template.queryForObject(sql,
				new PersonCursorExtractor(), "John");

		assertNotNull(person);
		assertEquals(Integer.valueOf(42), person.getAge());
		assertEquals("John", person.getFirstName());
		assertEquals("Doe", person.getLastName());
		assertEquals("Height", Double.valueOf("5.1d"), person.getHeight());
		List<Address> addresses = person.getAddresses();
		assertNotNull(addresses);
		assertEquals("Address size", 2, addresses.size());
	}

	// Currently passes
	public void testFailureQueryForObjectWithCursorExtractor() {
		String sql = "";
		CursorExtractor<Address> cursorExtractor = null;
		String args = "";
		try {
			template.queryForObject(sql, cursorExtractor, args);
			fail("Should throw an IllegalArgumentException because the cursorExtractor is null");
		} catch (IllegalArgumentException ie) {
		} catch (Exception e) {
			fail("Only an IllegalArgumentException should be thrown, not "
					+ e.getClass().getName());
		}

		cursorExtractor = new AddressCursorExtractor();
		try {
			template.queryForObject(sql, cursorExtractor, args);
			fail("Should throw an EmptySQLStatementException because the cursorExtractor is null");
		} catch (EmptySQLStatementException ie) {
		} catch (Exception e) {
			fail("Only an EmptySQLStatementException should be thrown "
					+ e.getMessage());
		}

		sql = "Select * from Person";
		try {
			template.queryForObject(sql, cursorExtractor, args);
			fail("Should throw an InvalidCursorExtractorException because the query does not return enough fields or from the Address table in a join clause");
		} catch (InvalidCursorException ie) {
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	// Currently passes
	public void testSuccessFindById() {
		Person person = template.findById(Person.class, 1L);
		assertJohnThePerson(person);
	}

	// Currently passes
	public void testFailedFindById() {
		Person person = template.findById(Person.class, 50L);
		assertNull(person);
	}

	// Currently passes
	public void testSuccessQueryWithDomainClass() {
		List<Person> people = template.query(
				"Select * from Person Where FIRST_NAME='?'", Person.class,
				"John");

		assertNotNull(people);
		assertEquals(1, people.size());
		Person person = people.get(0);
		assertJohnThePerson(person);
	}

	// Currently passes but needs more code, null sql, empty sql
	public void testFailureQueryWithDomainClass() {
		List<Person> people = template.query(
				"Select * from Person Where FIRST_NAME='?'", Person.class,
				"Jonathon");
		assertNotNull(people);
		assertEquals(0, people.size());

		// Null sql
		String sql = null;
		try {
			template.query(sql, Person.class, "Jonathon");
			fail("Should throw an EmptySQLStatementException because the query does not return enough fields or from the Address table in a join clause");
		} catch (EmptySQLStatementException esse) {
		} catch (Exception e) {
			fail(e.getMessage());
		}

		// Empty String sql
		sql = "";
		try {
			template.query(sql, Person.class, "Jonathon");
			fail("Should throw an EmptySQLStatementException because the query does not return enough fields or from the Address table in a join clause");
		} catch (EmptySQLStatementException esse) {
		} catch (Exception e) {
			fail(e.getMessage());
		}

		// Empty args
		sql = "Select * from Person Where FIRST_NAME='?'";
		try {
			template.query(sql, Person.class, (Object[]) null);
			fail("A DataAccessException should be thrown because the arguments for the where clause is null");
		} catch (DataAccessException dae) {
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	// Currently passes
	public void testSuccessQueryWithCursorRowMapper() {
		String sqlStart = "Select * from Person Where";
		CursorRowMapper<Person> personCursorRowMapper = new PersonCursorRowMapper();
		String sql = sqlStart + " FIRST_NAME='?'";
		List<Person> people = template
				.query(sql, personCursorRowMapper, "John");

		assertNotNull(people);
		assertEquals(1, people.size());
		Person person = people.get(0);
		assertJohnThePerson(person);

		List<Person> everyone = template.query("Select * from Person",
				Person.class);
		assertNotNull(everyone);
		Cursor cursor = dataBase.rawQuery("Select count(*) from Person", null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		int realCount = cursor.getInt(0);
		assertEquals(realCount, everyone.size());
	}

	// Currently passes add null sql or null rowmapper tests
	public void testFailuresQueryWithCursorRowMapper() {
		String sqlStart = "Select * from Person Where";
		CursorRowMapper<Person> personCursorRowMapper = new PersonCursorRowMapper();
		String sql = sqlStart + " FIRST_NAME=?";
		try {
			template.query(sql, personCursorRowMapper, "John");
			fail("A DataAccessException should be thrown because the String parameter is not quoted");
		} catch (DataAccessException dae) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		try {
			template.query("", Person.class);
			fail("A DataAccessException should be thrown because there is no query string");
		} catch (DataAccessException dae) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}

		try {
			template.query("Select * from Address", personCursorRowMapper);
			fail("A DataAccessException should be thrown because they are Addresses not People");
		} catch (DataAccessException dae) {
		} catch (Exception e) {
			fail("This should throw a Spring DataAccessException not one from SQLite "
					+ e.getMessage());
		}
	}

	private void assertJohnThePerson(Person john) {
		assertEquals("Age", Integer.valueOf(42), john.getAge());
		assertEquals("First name", "John", john.getFirstName());
		assertEquals("Last name", "Doe", john.getLastName());
		assertEquals("Height", Double.valueOf("5.1d"), john.getHeight());
		assertTrue("Staff", john.isStaff());
	}

	private void assertJaneThePerson(Person jane) {
		assertEquals("Age", Integer.valueOf(21), jane.getAge());
		assertEquals("First name", "Jane", jane.getFirstName());
		assertEquals("Last name", "Smith", jane.getLastName());
		assertEquals("Height", Double.valueOf("6.2D"), jane.getHeight());
		assertFalse("Staff", jane.isStaff());
	}

	private void assertAccount(Account account, String sql) {
		Cursor cursor = dataBase.rawQuery(sql, null);
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		cursor.moveToFirst();
		Account checkAccount = new Account();
		checkAccount.setAccountType(cursor.getString(cursor
				.getColumnIndex("ACCOUNT_TYPE")));
		checkAccount
				.setAmount(cursor.getDouble(cursor.getColumnIndex("AMOUNT")));
		checkAccount.setYearAccountOpened(cursor.getInt(cursor
				.getColumnIndex("YEAR_STARTED")));
		assertEquals(account, checkAccount);
	}
}
