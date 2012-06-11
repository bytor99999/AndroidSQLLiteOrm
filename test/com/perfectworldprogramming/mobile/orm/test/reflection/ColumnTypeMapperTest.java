package com.perfectworldprogramming.mobile.orm.test.reflection;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.exception.DataAccessException;
import com.perfectworldprogramming.mobile.orm.helper.DBHelper;
import com.perfectworldprogramming.mobile.orm.interfaces.ColumnTypeMapper;
import com.perfectworldprogramming.mobile.orm.reflection.AbstractMapper;
import com.perfectworldprogramming.mobile.orm.reflection.BooleanMapper;
import com.perfectworldprogramming.mobile.orm.reflection.DateMapper;
import com.perfectworldprogramming.mobile.orm.reflection.DoubleMapper;
import com.perfectworldprogramming.mobile.orm.reflection.FloatMapper;
import com.perfectworldprogramming.mobile.orm.reflection.IntegerMapper;
import com.perfectworldprogramming.mobile.orm.reflection.LongMapper;
import com.perfectworldprogramming.mobile.orm.reflection.PrimaryKeyMapper;
import com.perfectworldprogramming.mobile.orm.reflection.StringMapper;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;
import com.perfectworldprogramming.mobile.orm.test.interfaces.SampleDataHelper;

public class ColumnTypeMapperTest extends ActivityInstrumentationTestCase2<Main>
{

    AndroidSQLiteTemplate template;
    DBHelper helper;
    SQLiteDatabase dataBase;

    public ColumnTypeMapperTest()
    {
        super("org.springframework.mobile.orm.test", Main.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setUp()
    {
        helper = new DBHelper(this.getInstrumentation().getContext(), new Class[] { Person.class, Address.class, Account.class }, "ormtest", 3);
        template = new AndroidSQLiteTemplate(helper.getSqlLiteDatabase());
        dataBase = helper.getSqlLiteDatabase();
        SampleDataHelper.addDataToDatabase(template);
    }

    @Override
    protected void tearDown() throws Exception
    {
        helper.cleanup();
    }

    public void testAbstractMapperFieldModelToDatabase()
    {
        AbstractMapper<?> mapper = (AbstractMapper<?>) StringMapper.INSTANCE;

        Address address = new Address();
        address.setZipCode("2468");
        Field field = getField(Address.class, "zipCode");
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase(field, address, cv);
        doTestContentValues(cv, "ZIP_CODE", "2468");
    }

    public void testAbstractMapperColumnModelToDatabase()
    {
        AbstractMapper<?> mapper = (AbstractMapper<?>) StringMapper.INSTANCE;

        Address address = new Address();
        address.setZipCode("13579");
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("ZIP_CODE", address, cv);
        doTestContentValues(cv, "ZIP_CODE", "13579");
    }

    public void testAbstractMapperFieldDatabaseToModel()
    {
        AbstractMapper<?> mapper = (AbstractMapper<?>) StringMapper.INSTANCE;

        Address address = new Address();
        Field field = getField(Address.class, "zipCode");
        Cursor cursor = dataBase.rawQuery("Select * from Address where ADDRESS_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, field, address);
        assertEquals("Zip Code", "90808", address.getZipCode());
    }

    public void testAbstractMapperColumnDatabaseToModel()
    {
        AbstractMapper<?> mapper = (AbstractMapper<?>) StringMapper.INSTANCE;
        Address address = new Address();
        Cursor cursor = dataBase.rawQuery("Select * from Address where ADDRESS_ID = 2", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "ZIP_CODE", address);
        assertEquals("Zip Code", "12345", address.getZipCode());
    }

    public void testMapNullPrimaryKeyModelToDatabase()
    {
        ColumnTypeMapper<Long> mapper = PrimaryKeyMapper.INSTANCE;
        
        Person person = new Person();
        // Primary key
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("PERSON_ID", person, cv);
        doTestNullContentValues(cv, "PERSON_ID", true);
    }

    public void testMapNullStringModelToDatabase()
    {
        Person person = new Person();
        ColumnTypeMapper<String> mapper = StringMapper.INSTANCE;
        // String
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase(Person.COL_FIRST_NAME, person, cv);
        doTestNullContentValues(cv, Person.COL_FIRST_NAME, true);
        Field field = getField(Person.class, "firstName");
        doTestNullContentValues(cv, field, true);
    }

    public void testMapNullIntegerModelToDatabase()
    {
        Person person = new Person();
        // Integer and int primitive
        ColumnTypeMapper<Integer> mapper = IntegerMapper.INSTANCE;
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("AGE", person, cv);
        doTestNullContentValues(cv, "AGE", true);
        Field field = getField(Person.class, "age");
        doTestNullContentValues(cv, field, true);

        cv = new ContentValues();
        mapper.modelToDatabase("JACKET_SIZE", person, cv);
        doTestNullContentValues(cv, "JACKET_SIZE", false);
        assertEquals("Jacket size", "0", cv.get("JACKET_SIZE"));
        field = getField(Person.class, "jacketSize");
        doTestNullContentValues(cv, field, false);
    }

    public void testMapNullLongModelToDatabase()
    {
        Person person = new Person();
        ColumnTypeMapper<Long> mapper = LongMapper.INSTANCE;
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("DEPENDANTS", person, cv);
        doTestNullContentValues(cv, "DEPENDANTS", true);
        Field field = getField(Person.class, "dependants");
        doTestNullContentValues(cv, field, true);

        cv = new ContentValues();
        mapper.modelToDatabase("PETS", person, cv);
        doTestNullContentValues(cv, "PETS", false);
        assertEquals("Pets", "0", cv.get("PETS"));
        field = getField(Person.class, "pets");
        doTestNullContentValues(cv, field, false);
    }
    public void testMapNullFloatModelToDatabase()
    {
        Person person = new Person();
        ColumnTypeMapper<Float> mapper = FloatMapper.INSTANCE;
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("WEIGHT", person, cv);
        doTestNullContentValues(cv, "WEIGHT", true);
        Field field = getField(Person.class, "weight");
        doTestNullContentValues(cv, field, true);

        cv = new ContentValues();
        mapper.modelToDatabase("SHOE_SIZE", person, cv);
        doTestNullContentValues(cv, "SHOE_SIZE", false);
        assertEquals("Shoe size", "0.0", cv.get("SHOE_SIZE"));
        field = getField(Person.class, "shoeSize");
        doTestNullContentValues(cv, field, false);
    }
    public void testMapNullDoubleModelToDatabase()
    {
        Person person = new Person();
        ColumnTypeMapper<Double> mapper = DoubleMapper.INSTANCE;
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("HEIGHT", person, cv);
        doTestNullContentValues(cv, "HEIGHT", true);
        Field field = getField(Person.class, "height");
        doTestNullContentValues(cv, field, true);

        cv = new ContentValues();
        mapper.modelToDatabase("WEALTH", person, cv);
        doTestNullContentValues(cv, "WEALTH", false);
        assertEquals("Wealth", "0.0", cv.get("WEALTH"));
        field = getField(Person.class, "wealth");
        doTestNullContentValues(cv, field, false);
    }
    public void testMapNullBooleanModelToDatabase()
    {
        Person person = new Person();
        ColumnTypeMapper<Boolean> mapper = BooleanMapper.INSTANCE;
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("FEMALE", person, cv);
        doTestNullContentValues(cv, "FEMALE", true);
        Field field = getField(Person.class, "female");
        doTestNullContentValues(cv, field, true);

        cv = new ContentValues();
        mapper.modelToDatabase("STAFF", person, cv);
        doTestNullContentValues(cv, "STAFF", false);
        assertEquals("Staff", Integer.valueOf(0), cv.get("STAFF"));
        field = getField(Person.class, "staff");
        doTestNullContentValues(cv, field, false);
    }

    //TODO null String mapping, requires a nullable String field in the DB
    public void t_estMapNullStringDatabaseToModel()
    {
        long nullId = template.insert(new Person());
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = "+nullId, null);
        cursor.moveToFirst();
        Person person = new Person();
        ColumnTypeMapper<String> mapper = StringMapper.INSTANCE;

        mapper.databaseToModel(cursor, Person.COL_FIRST_NAME, person);
        assertNull("First name", person.getFirstName());
        Field field = getField(Person.class, "firstName");
        mapper.databaseToModel(cursor, field, person);
        assertNull("First name", person.getFirstName());
        
        //template.delete("Select * from Person where PERSON_ID = "+nullId, (Object[])null);
    }

    public void testMapNullIntegerDatabaseToModel()
    {
        final Person nullPerson = new Person("first", "last", 10, 5.5D);
        long nullId = template.insert(nullPerson);
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = "+nullId, null);
        cursor.moveToFirst();
        Person person = new Person();
        ColumnTypeMapper<Integer> mapper = IntegerMapper.INSTANCE;

        //TODO null Integer wrapper mapping
//        mapper.databaseToModel(cursor, "AGE", person);
//        assertNull("Age", person.getAge());
//        Field field = getField(Person.class, "age");
//        mapper.databaseToModel(cursor, field, person);
//        assertNull("Age", person.getAge());

        mapper.databaseToModel(cursor, "JACKET_SIZE", person);
        assertEquals("Jacket size", 0, person.getJacketSize());
        Field field = getField(Person.class, "jacketSize");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Jacket size", 0, person.getJacketSize());

        template.delete(nullPerson);
    }

    public void testMapNullLongDatabaseToModel()
    {
        final Person nullPerson = new Person("first", "last", 10, 5.5D);
        long nullId = template.insert(nullPerson);
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = "+nullId, null);
        cursor.moveToFirst();
        ColumnTypeMapper<Long> mapper = LongMapper.INSTANCE;
        Person person = new Person();

        mapper.databaseToModel(cursor, "DEPENDANTS", person);
        assertNull("Dependants", person.getDependants());
        Field field = getField(Person.class, "dependants");
        mapper.databaseToModel(cursor, field, person);
        assertNull("Dependants", person.getDependants());

        mapper.databaseToModel(cursor, "PETS", person);
        assertEquals("Pets", 0L, person.getPets());
        field = getField(Person.class, "pets");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Pets", 0L, person.getPets());

        template.delete(nullPerson);
    }

    public void testMapNullFloatDatabaseToModel()
    {
        final Person nullPerson = new Person("first", "last", 10, 5.5D);
        long nullId = template.insert(nullPerson);
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = "+nullId, null);
        cursor.moveToFirst();
        ColumnTypeMapper<Float> mapper = FloatMapper.INSTANCE;
        Person person = new Person();

        mapper.databaseToModel(cursor, "WEIGHT", person);
        assertNull("Weight", person.getWeight());
        Field field = getField(Person.class, "weight");
        mapper.databaseToModel(cursor, field, person);
        assertNull("Weight", person.getWeight());

        mapper.databaseToModel(cursor, "SHOE_SIZE", person);
        assertEquals("Shoe size", 0F, person.getShoeSize());
        field = getField(Person.class, "shoeSize");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Shoe size", 0F, person.getShoeSize());

        template.delete(nullPerson);
    }

    public void testMapNullDoubleDatabaseToModel()
    {
        final Person nullPerson = new Person("first", "last", 10, 5.5D);
        long nullId = template.insert(nullPerson);
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = "+nullId, null);
        cursor.moveToFirst();
        ColumnTypeMapper<Double> mapper = DoubleMapper.INSTANCE;
        Person person = new Person();

        //TODO null Double wrapper mapping
//        mapper.databaseToModel(cursor, "HEIGHT", person);
//        assertNull("Height", person.getHeight());
//        Field field = getField(Person.class, "height");
//        mapper.databaseToModel(cursor, field, person);
//        assertNull("Height", person.getHeight());

        mapper.databaseToModel(cursor, "WEALTH", person);
        assertEquals("Wealth", 0D, person.getWealth());
        Field field = getField(Person.class, "wealth");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Wealth", 0D, person.getWealth());

        template.delete(nullPerson);
    }

    public void testMapNullBooleanDatabaseToModel()
    {
        final Person nullPerson = new Person("first", "last", 10, 5.5D);
        long nullId = template.insert(nullPerson);
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = "+nullId, null);
        cursor.moveToFirst();
        ColumnTypeMapper<Boolean> mapper = BooleanMapper.INSTANCE;
        Person person = new Person();

        mapper.databaseToModel(cursor, "FEMALE", person);
        assertNull("Female", person.getFemale());
        Field field = getField(Person.class, "female");
        mapper.databaseToModel(cursor, field, person);
        assertNull("Female", person.getFemale());

        mapper.databaseToModel(cursor, "STAFF", person);
        assertEquals("Staff", false, person.isStaff());
        field = getField(Person.class, "staff");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Staff", false, person.isStaff());

        template.delete(nullPerson);
    }

    // TODO public void testMapNullDateModelToDatabase()
    // TODO public void testMapNullBlobModelToDatabase()

    public void testBooleanMapperDatabaseToModel()
    {
        ColumnTypeMapper<Boolean> mapper = BooleanMapper.INSTANCE;
        Field fieldStaff = getField(Person.class, "staff");
        Field fieldFemale = getField(Person.class, "female");

        assertEquals("DatabaseColumnType", "INTEGER", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "STAFF", person);
        assertEquals("Staff", true, person.isStaff());
        mapper.databaseToModel(cursor, "FEMALE", person);
        assertEquals("Female", Boolean.FALSE, person.getFemale());
        person = new Person();
        mapper.databaseToModel(cursor, fieldStaff, person);
        assertEquals("Staff", true, person.isStaff());
        mapper.databaseToModel(cursor, fieldFemale, person);
        assertEquals("Female", Boolean.FALSE, person.getFemale());

        person = new Person();
        cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 2", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "STAFF", person);
        assertEquals("Staff", false, person.isStaff());
        mapper.databaseToModel(cursor, "FEMALE", person);
        assertEquals("Female", Boolean.TRUE, person.getFemale());
        person = new Person();
        mapper.databaseToModel(cursor, fieldStaff, person);
        assertEquals("Staff", false, person.isStaff());
        mapper.databaseToModel(cursor, fieldFemale, person);
        assertEquals("Female", Boolean.TRUE, person.getFemale());
    }

    public void testBooleanMapperModelToDatabase()
    {
        ColumnTypeMapper<Boolean> mapper = BooleanMapper.INSTANCE;
        Field field = getField(Person.class, "staff");

        Person person = new Person();
        person.setStaff(true);
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("STAFF", person, cv);
        doTestContentValues(cv, "STAFF", Integer.valueOf(1)); // maps to 1 in the DB
        cv = new ContentValues();
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "STAFF", Integer.valueOf(1));

        person = new Person();
        person.setStaff(false);
        cv = new ContentValues();
        mapper.modelToDatabase("STAFF", person, cv);
        doTestContentValues(cv, "STAFF", Integer.valueOf(0));
        cv = new ContentValues();
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "STAFF", Integer.valueOf(0));
    }

    public void testStringMapperDatabaseToModel()
    {
        ColumnTypeMapper<String> mapper = StringMapper.INSTANCE;
        Field field = getField(Person.class, "firstName");

        assertEquals("DatabaseColumnType", "TEXT", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, Person.COL_FIRST_NAME, person);
        assertEquals("First name", "John", person.getFirstName());
        person.setFirstName(null);
        mapper.databaseToModel(cursor, field, person);
        assertEquals("First name", "John", person.getFirstName());
    }

    public void testStringMapperModelToDatabase()
    {
        ColumnTypeMapper<String> mapper = StringMapper.INSTANCE;
        Field field = getField(Person.class, "firstName");

        Person person = new Person();
        person.setFirstName("John");
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase(Person.COL_FIRST_NAME, person, cv);
        doTestContentValues(cv, Person.COL_FIRST_NAME, "John");
        cv = new ContentValues();
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, Person.COL_FIRST_NAME, "John");
    }

    public void testDoubleMapperDatabaseToModel()
    {
        ColumnTypeMapper<Double> mapper = DoubleMapper.INSTANCE;

        assertEquals("DatabaseColumnType", "REAL", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "HEIGHT", person);
        assertEquals("Height", Double.valueOf(5.1D), person.getHeight());
        person.setHeight(null);
        Field field = getField(Person.class, "height");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Height", Double.valueOf(5.1D), person.getHeight());

        mapper.databaseToModel(cursor, "WEALTH", person);
        assertEquals("Wealth", 210000.00D, person.getWealth());
        person.setWealth(0D);
        field = getField(Person.class, "wealth");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Wealth", 210000.00D, person.getWealth());
    }
    
    public void testDoubleMapperModelToDatabase()
    {
        ColumnTypeMapper<Double> mapper = DoubleMapper.INSTANCE;
        Person person = new Person();
        person.setHeight(Double.valueOf(5.2D));
        person.setWealth(Double.valueOf(10000.20D));
 
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("HEIGHT", person, cv);
        doTestContentValues(cv, "HEIGHT", "5.2");
        cv = new ContentValues();
        Field field = getField(Person.class, "height");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "HEIGHT", "5.2");

        cv = new ContentValues();
        mapper.modelToDatabase("WEALTH", person, cv);
        doTestContentValues(cv, "WEALTH", "10000.2");
        cv = new ContentValues();
        field = getField(Person.class, "wealth");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "WEALTH", "10000.2");
    }
    
    public void testFloatMapperDatabaseToModel()
    {
        ColumnTypeMapper<Float> mapper = FloatMapper.INSTANCE;

        assertEquals("DatabaseColumnType", "REAL", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "WEIGHT", person);
        assertEquals("Weight", Float.valueOf(175.6F), person.getWeight());
        person.setWeight(null);
        Field field = getField(Person.class, "weight");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Weight", Float.valueOf(175.6F), person.getWeight());

        mapper.databaseToModel(cursor, "SHOE_SIZE", person);
        assertEquals("Shoe size", 9.5F, person.getShoeSize());
        person.setShoeSize(0F);
        field = getField(Person.class, "shoeSize");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Shoe size", 9.5F, person.getShoeSize());
    }

    public void testFloatMapperModelToDatabase()
    {
        ColumnTypeMapper<Float> mapper = FloatMapper.INSTANCE;
        Person person = new Person();
        person.setWeight(Float.valueOf(55.2F));
        person.setShoeSize(7.10F);
 
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("WEIGHT", person, cv);
        doTestContentValues(cv, "WEIGHT", "55.2");
        cv = new ContentValues();
        Field field = getField(Person.class, "weight");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "WEIGHT", "55.2");

        cv = new ContentValues();
        mapper.modelToDatabase("SHOE_SIZE", person, cv);
        doTestContentValues(cv, "SHOE_SIZE", "7.1");
        cv = new ContentValues();
        field = getField(Person.class, "shoeSize");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "SHOE_SIZE", "7.1");
    }

    public void testIntegerMapperDatabaseToModel()
    {
        ColumnTypeMapper<Integer> mapper = IntegerMapper.INSTANCE;

        assertEquals("DatabaseColumnType", "INTEGER", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "AGE", person);
        assertEquals("Age", Integer.valueOf(42), person.getAge());
        person.setAge(0);
        Field field = getField(Person.class, "age");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Age", Integer.valueOf(42), person.getAge());

        mapper.databaseToModel(cursor, "JACKET_SIZE", person);
        assertEquals("Jacket size", 43, person.getJacketSize());
        person.setJacketSize(0);
        field = getField(Person.class, "jacketSize");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Jacket size", 43, person.getJacketSize());
    }

    public void testIntegerMapperModelToDatabase()
    {
        ColumnTypeMapper<Integer> mapper = IntegerMapper.INSTANCE;
        Person person = new Person();
        person.setAge(Integer.valueOf(55));
        person.setJacketSize(56);
 
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("AGE", person, cv);
        doTestContentValues(cv, "AGE", "55");
        cv = new ContentValues();
        Field field = getField(Person.class, "age");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "AGE", "55");

        cv = new ContentValues();
        mapper.modelToDatabase("JACKET_SIZE", person, cv);
        doTestContentValues(cv, "JACKET_SIZE", "56");
        cv = new ContentValues();
        field = getField(Person.class, "jacketSize");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "JACKET_SIZE", "56");
    }

    public void testLongMapperDatabaseToModel()
    {
        ColumnTypeMapper<Long> mapper = LongMapper.INSTANCE;

        assertEquals("DatabaseColumnType", "INTEGER", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "DEPENDANTS", person);
        assertEquals("Dependants", Long.valueOf(2), person.getDependants());
        person.setDependants(-1L);
        Field field = getField(Person.class, "dependants");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Dependants", Long.valueOf(2), person.getDependants());

        mapper.databaseToModel(cursor, "PETS", person);
        assertEquals("Pets", 1L, person.getPets());
        person.setPets(-1L);
        field = getField(Person.class, "pets");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Pets", 1l, person.getPets());
    }

    public void testLongMapperModelToDatabase()
    {
        ColumnTypeMapper<Long> mapper = LongMapper.INSTANCE;
        Person person = new Person();
        person.setDependants(Long.valueOf(57));
        person.setPets(58L);
 
        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("DEPENDANTS", person, cv);
        doTestContentValues(cv, "DEPENDANTS", "57");
        cv = new ContentValues();
        Field field = getField(Person.class, "dependants");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "DEPENDANTS", "57");

        cv = new ContentValues();
        mapper.modelToDatabase("PETS", person, cv);
        doTestContentValues(cv, "PETS", "58");
        cv = new ContentValues();
        field = getField(Person.class, "pets");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "PETS", "58");
    }

    public void testDateMapperDatabaseToModel()
    {
        ColumnTypeMapper<java.sql.Date> mapper = DateMapper.INSTANCE;

        assertEquals("DatabaseColumnType", "INTEGER", mapper.getDatabaseColumnType());

        Person person = new Person();
        Cursor cursor = dataBase.rawQuery("Select * from Person where PERSON_ID = 1", null);
        cursor.moveToFirst();
        mapper.databaseToModel(cursor, "START_DATE", person);
        assertEquals("Start Date", new java.sql.Date(2012-1900, 1, 1), person.getStartDate());
        person.setStartDate(null);
        Field field = getField(Person.class, "startDate");
        mapper.databaseToModel(cursor, field, person);
        assertEquals("Start Date", new java.sql.Date(2012-1900, 1, 1), person.getStartDate());
    }

    public void testDateMapperModelToDatabase()
    {
        ColumnTypeMapper<java.sql.Date> mapper = DateMapper.INSTANCE;
        Person person = new Person();
        
        // year specified as year-1900
        person.setStartDate(new java.sql.Date(2012-1900, 4, 5));

        ContentValues cv = new ContentValues();
        mapper.modelToDatabase("START_DATE", person, cv);
        doTestContentValues(cv, "START_DATE", 1336147200000L);
        cv = new ContentValues();
        Field field = getField(Person.class, "startDate");
        mapper.modelToDatabase(field, person, cv);
        doTestContentValues(cv, "START_DATE", 1336147200000L);
    }
    // TODO public void testMapBlobDatabaseToModel()
    // TODO public void testMapBlobModelToDatabase()


    private Field getField(Class<?> clazz, String name)
    {
        try
        {
            return clazz.getDeclaredField(name);
        }
        catch (SecurityException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        catch (NoSuchFieldException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void doTestContentValues(ContentValues cv, String key, Object value)
    {
        assertEquals("ContentValues size", 1, cv.size());
        assertTrue("ContentValues key", cv.containsKey(key));
        assertEquals("ContentValues value", value, cv.get(key));
    }
    private void doTestNullContentValues(ContentValues cv, String key, boolean expectNull)
    {
        assertEquals("ContentValues size", expectNull?0:1, cv.size());
        assertEquals("ContentValues key", !expectNull, cv.containsKey(key));
    }
    private void doTestNullContentValues(ContentValues cv, Field key, boolean expectNull)
    {
        doTestNullContentValues(cv, key.getAnnotation(Column.class).value(), expectNull);
    }
}
