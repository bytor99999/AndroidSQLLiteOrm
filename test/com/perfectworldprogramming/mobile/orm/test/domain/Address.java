package com.perfectworldprogramming.mobile.orm.test.domain;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ColumnType;
import com.perfectworldprogramming.mobile.orm.annotations.ForeignKey;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;

/**
 * User: Mark Spritzler
 * Date: 3/14/11
 * Time: 2:42 PM
 */
public class Address {

    @PrimaryKey(value = "ADDRESS_ID")
    private Long id;

    @Column(value = "STREET", type = ColumnType.TEXT, nullable = false)
    private String street;

    @Column(value = "CITY", type = ColumnType.TEXT, nullable = false)
    private String city;

    @Column(value = "STATE", type = ColumnType.TEXT, nullable = false)
    private String state;

    @Column(value = "ZIP_CODE", type = ColumnType.TEXT, nullable = false)
    private String zipCode;

    @ForeignKey(value = "PERSON_ID")
    private Person person;

    public Address() {}

    public Address(Long id) {
        this.id = id;
    }

    public Address(String street, String city, String state, String zipCode, Person person) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    @Override
    public String toString() {
    	String address = "Id: " + id +
    					" Street: " + street +
    					" City: " + city +
    					" State: " + state +
    					" Zip Code: " + zipCode;    	
    	return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (state != null ? !state.equals(address.state) : address.state != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        return result;
    }
}
