package com.perfectworldprogramming.mobile.orm.test.oql;

import java.util.ArrayList;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;

import com.perfectworldprogramming.mobile.orm.exception.FieldNotFoundException;
import com.perfectworldprogramming.mobile.orm.oql.QueryParser;
import com.perfectworldprogramming.mobile.orm.test.Main;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

public class QueryParserTest  extends ActivityInstrumentationTestCase2<Main>
{
    public QueryParserTest()
    {
        super("org.springframework.mobile.orm.test", Main.class);
    }

    public void testUnchanged()
    {
        final String input = "select * from Person";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        assertEquals(input, QueryParser.parse(input, singleton));
    }

    public void testUnchanged2()
    {
        final String input = "select * from Person where FIRST_NAME = ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        assertEquals(input, QueryParser.parse(input, singleton));
    }

    public void testSingleSimple()
    {
        final String input = "select * from Person where [firstName] = ?";
        final String expected = "select * from Person where FIRST_NAME = ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        final String actual = QueryParser.parse(input, singleton);
        assertEquals(expected, actual);
    }

    public void testMultipleSimple()
    {
        final String input = "select * from Person where [firstName] = ? and [age] < ?";
        final String expected = "select * from Person where FIRST_NAME = ? and AGE < ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        final String actual = QueryParser.parse(input, singleton);
        assertEquals(expected, actual);
    }

    public void testSingleComplex()
    {
        final String input = "select * from Person where [Person.firstName] = ?";
        final String expected = "select * from Person where FIRST_NAME = ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        final String actual = QueryParser.parse(input, singleton);
        assertEquals(expected, actual);
    }

    public void testMultipleComplex()
    {
        final String input = "select * from Person where [Person.firstName] = ? and [Person.age] < ?";
        final String expected = "select * from Person where FIRST_NAME = ? and AGE < ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        final String actual = QueryParser.parse(input, singleton);
        assertEquals(expected, actual);
    }

    public void testFailSimpleMatchInvalidField()
    {
        final String input = "select * from Person where [abcdef] = ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        try
        {
            QueryParser.parse(input, singleton);
            fail("FieldNotFound expected");
        }
        catch(FieldNotFoundException e)
        {
            // expected
        }
    }
    public void testFailSimpleMatchInvalidClass()
    {
        final String input = "select * from Person where [age] = ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Address.class);
        try
        {
            QueryParser.parse(input, singleton);
            fail("FieldNotFound expected");
        }
        catch(FieldNotFoundException e)
        {
            // expected
        }
    }

    public void testFailSimpleMatchInvalidClassAndField()
    {
        final String input = "select * from Person where [Address.age] = ?";
        List<Class<?>> singleton = new ArrayList<Class<?>>();
        singleton.add(Person.class);
        singleton.add(Address.class);
        try
        {
            QueryParser.parse(input, singleton);
            fail("FieldNotFound expected");
        }
        catch(FieldNotFoundException e)
        {
            // expected
        }
    }

    public void testMultipleClasses()
    {
        final String input = "select * from Person p, Address a where p.[lastName] = ? and p.[Person.id]=a.[Address.person]";
        final String expected = "select * from Person p, Address a where p.LAST_NAME = ? and p.PERSON_ID=a.PERSON_ID";
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Person.class);
        list.add(Address.class);
        final String actual = QueryParser.parse(input, list);
        assertEquals(expected, actual);
    }

    public void testForeignKeyJoin()
    {
        final String input = "select * from Person p, Address a where p.[Person]=a.[Address.person]";
        final String expected = "select * from Person p, Address a where p.PERSON_ID=a.PERSON_ID";
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Person.class);
        list.add(Address.class);
        final String actual = QueryParser.parse(input, list);
        assertEquals(expected, actual);
    }
}
