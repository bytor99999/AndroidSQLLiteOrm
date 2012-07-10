package com.perfectworldprogramming.mobile.orm.test.interfaces;

import java.util.ArrayList;
import java.util.List;


import com.perfectworldprogramming.mobile.orm.AndroidSQLiteTemplate;
import com.perfectworldprogramming.mobile.orm.test.domain.Account;
import com.perfectworldprogramming.mobile.orm.test.domain.Address;
import com.perfectworldprogramming.mobile.orm.test.domain.Person;

/**
 * User: Mark Spritzler
 * Date: 4/6/11
 * Time: 11:11 AM
 */
public class SampleDataHelper {
	
	private static List<Account> sampleAccounts;

	public static void addDataToDatabase(AndroidSQLiteTemplate template) {
    	int addressCount = template.queryForInt("Select count(*) from Address");
    	int personCount = template.queryForInt("Select count(*) from Person");
    	int accountCount = template.queryForInt("Select count(*) from Account"); 
    	if (addressCount > 0) {
    		template.delete("DELETE FROM Address", (Object[])null);
    	}
    	if (personCount > 0) {
    		template.delete("DELETE FROM Person", (Object[])null);
    	}
    	if (accountCount > 0) {
    		template.delete("DELETE FROM Account", (Object[])null);
    	}
    	List<Address> sampleAddress = createAddresses();
    	List<Person> samplePeople = createPeople();
    	sampleAccounts = createAccounts();
    	
    	Person person1 = samplePeople.get(0);
    	for (Address address : sampleAddress) {
    		person1.addAddress(address);
    	}

    	for (Person person : samplePeople) {
    		template.insert(person);
    	}
    	
    	for (Address address : sampleAddress) {
    		template.insert(address);
    	}
    	
    	for (Account account : sampleAccounts) {
    		template.insert(account);
    	}

    }
	
	public static List<Account> getAccounts() {
		return sampleAccounts;
	}
	
    private static List<Address> createAddresses() {
        List<Address> sampleAddress = new ArrayList<Address>();
        Address address1 = new Address();
        address1.setZipCode("90808");
        address1.setCity("Long Beach");
        address1.setState("CA");
        address1.setStreet("123 Elm Street");
        sampleAddress.add(address1);

        Address address2 = new Address();
        address2.setZipCode("12345");
        address2.setCity("Philadelphia");
        address2.setState("PA");
        address2.setStreet("123 Broad Street");
        sampleAddress.add(address2);
        return sampleAddress;
    }

    private static List<Person> createPeople() {
        List<Person> samplePeople = new ArrayList<Person>();
        Person person1 = new Person();
        person1.setAge(42);
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setHeight(5.1D);
        person1.setWeight(175.6F);
        person1.setJacketSize(43);
        person1.setShoeSize(9.5F);
        person1.setWealth(210000.00D);
        person1.setStaff(true);
        person1.setFemale(false);
        person1.setDependants(2L);
        person1.setPets(1L);
        person1.setStartDate(new java.sql.Date(2012-1900, 1, 1));
        samplePeople.add(person1);

        Person person2 = new Person();
        person2.setAge(21);
        person2.setHeight(6.2D);
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setWeight(225.6F);
        person2.setJacketSize(48);
        person2.setShoeSize(12.5F);
        person2.setWealth(10000.00D);
        person2.setStaff(false);
        person2.setFemale(true);
        person2.setDependants(0L);
        person2.setPets(3L); // cat lady
        person2.setStartDate(new java.sql.Date(2012-1900, 2, 3));
        samplePeople.add(person2);

        return samplePeople;
    }
    
    private static List<Account> createAccounts() {
    	List<Account> accounts = new ArrayList<Account>();
    	//For successful delete
    	Account account1 = new Account();
    	account1.setAccountType("Personal");
    	account1.setAmount(23.00D);
    	account1.setYearAccountOpened(1989);
    	accounts.add(account1);
    	
    	//For successful delete
    	Account account2 = new Account();
    	account2.setAccountType("Business");
    	account2.setAmount(560000.00D);
    	account2.setYearAccountOpened(1990);
    	accounts.add(account2);
    	    	
    	// for successful delete
    	Account account3 = new Account();
    	account3.setAccountType("Business");
    	account3.setAmount(475.60D);
    	account3.setYearAccountOpened(1991);
    	accounts.add(account3);
    	
    	// for successful delete
    	Account account4 = new Account();
    	account4.setAccountType("Business");
    	account4.setAmount(4200.00D);
    	account4.setYearAccountOpened(2000);
    	accounts.add(account4);
    	
    	// for successful update
    	Account account5 = new Account();
    	account5.setAccountType("Business Plus");
    	account5.setAmount(4200.00D);
    	account5.setYearAccountOpened(2001);
    	accounts.add(account5);
    	
    	// for successful update
    	Account account6 = new Account();
    	account6.setAccountType("Personal Plus");
    	account6.setAmount(4200.00D);
    	account6.setYearAccountOpened(2002);
    	accounts.add(account6);
    	
    	// for successful update
    	Account account7 = new Account();
    	account7.setAccountType("Personal Plus");
    	account7.setAmount(4200.00D);
    	account7.setYearAccountOpened(2003);
    	accounts.add(account7);	
    	
    	// for successful update
    	Account account8 = new Account();
    	account8.setAccountType("Business Plus");
    	account8.setAmount(4200.00D);
    	account8.setYearAccountOpened(2011);
    	accounts.add(account8);
    	
    	return accounts;
    }
    
}
