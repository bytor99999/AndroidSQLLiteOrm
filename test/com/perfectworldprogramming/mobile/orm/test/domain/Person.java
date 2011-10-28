package com.perfectworldprogramming.mobile.orm.test.domain;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ColumnType;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;
import com.perfectworldprogramming.mobile.orm.annotations.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Mark Spritzler
 * Date: 3/14/11
 * Time: 2:42 PM
 */
public class Person {

    @PrimaryKey(value = "PERSON_ID")
    private Long id;

    @Column(value = "FIRST_NAME", type = ColumnType.TEXT, nullable = false)
    private String firstName;

    @Column(value = "LAST_NAME", type = ColumnType.TEXT, nullable = false)
    private String lastName;
    
    @Column(value = "AGE", type = ColumnType.INTEGER, nullable = false)
    private Integer age;

    @Column(value = "HEIGHT", type = ColumnType.REAL)
    private Double height;
    
    @Column(value = "WEIGHT", type = ColumnType.REAL)
    private Float weight;

    @Column(value = "JACKET_SIZE", type = ColumnType.INTEGER)
    private int jacketSize;
    
    @Column(value = "SHOE_SIZE", type = ColumnType.REAL)
    private float shoeSize;

    @Column(value = "WEALTH", type = ColumnType.REAL)
    private double wealth;
    
    

    @Transient
    private List<Address> addresses;

    public Person() {}

    public Person(Long id) {
        this.id = id;
    }

    public Person(String firstName, String lastName, int age, Double height) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public int getJacketSize() {
		return jacketSize;
	}

	public void setJacketSize(int jacketSize) {
		this.jacketSize = jacketSize;
	}

	public float getShoeSize() {
		return shoeSize;
	}

	public void setShoeSize(float shoeSize) {
		this.shoeSize = shoeSize;
	}

	public double getWealth() {
		return wealth;
	}

	public void setWealth(double wealth) {
		this.wealth = wealth;
	}

	public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address address) {
        if (addresses == null) {
            this.addresses = new ArrayList<Address>();
        }
        this.addresses.add(address);
        address.setPerson(this);
    }
    
    @Override
    public String toString() {
    	String person = "Id: " + id +
    					" First Name: " + firstName +
    					" Last Name: " + lastName +
    					" Age: " + age +
    					" Height: " + height +
    					" Weight: " + weight + 
    					" Jacket Size: " + jacketSize +
    					" Shoe Size: " + shoeSize +
    					" Wealth: " + wealth;
    	if (addresses != null && addresses.size() > 0) {
    		String stringOfAddresses = "\t Addresses: \n";
    		for (Address address : addresses) {
    			stringOfAddresses += "\tt " + address; 
    		}
    		person += stringOfAddresses;	
    	}
    	return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        if (height != null ? !height.equals(person.height) : person.height != null) return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }
}
