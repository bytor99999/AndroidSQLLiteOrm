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

    public static final String PK_PERSON ="PERSON_ID";
    public static final String COL_FIRST_NAME ="FIRST_NAME";
    public static final String COL_LAST_NAME ="LAST_NAME";
    public static final String COL_AGE ="AGE";
    public static final String COL_JACKET_SIZE ="JACKET_SIZE";
    public static final String COL_DEPENDANTS ="DEPENDANTS";
    public static final String COL_PETS ="PETS";
    public static final String COL_STAFF ="STAFF";
    public static final String COL_FEMALE ="FEMALE";
    public static final String COL_WEIGHT ="WEIGHT";
    public static final String COL_SHOE_SIZE ="SHOE_SIZE";
    public static final String COL_HEIGHT ="HEIGHT";
    public static final String COL_WEALTH ="WEALTH";
    public static final String COL_START_DATE ="START_DATE";
    
    @PrimaryKey(value = PK_PERSON)
    private Long id;

    @Column(value = COL_FIRST_NAME, type = ColumnType.STRING, nullable = false)
    private String firstName;

    @Column(value = COL_LAST_NAME, type = ColumnType.STRING, nullable = false)
    private String lastName;
    
    @Column(value = COL_AGE, type = ColumnType.INTEGER, nullable = false)
    private Integer age;

    @Column(value = COL_JACKET_SIZE, type = ColumnType.INTEGER)
    private int jacketSize;

    @Column(value = COL_HEIGHT, type = ColumnType.DOUBLE)
    private Double height;
    
    @Column(value = COL_WEIGHT, type = ColumnType.FLOAT)
    private Float weight;

    @Column(value = COL_DEPENDANTS, type = ColumnType.LONG)
    private Long dependants;

    @Column(value = COL_PETS, type = ColumnType.LONG)
    private long pets;
    
    @Column(value = COL_SHOE_SIZE, type = ColumnType.FLOAT)
    private float shoeSize;

    @Column(value = COL_WEALTH, type = ColumnType.DOUBLE)
    private double wealth;
    
    @Column(value = COL_STAFF, type = ColumnType.BOOLEAN)
    private boolean staff;

    @Column(value = COL_FEMALE, type = ColumnType.BOOLEAN)
    private Boolean female;

    @Column(value = COL_START_DATE, type = ColumnType.DATE)
    private java.sql.Date startDate;

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

    public boolean isStaff()
    {
        return staff;
    }

    public void setStaff(boolean staff)
    {
        this.staff = staff;
    }

    public Long getDependants()
    {
        return dependants;
    }

    public void setDependants(Long dependants)
    {
        this.dependants = dependants;
    }

    public long getPets()
    {
        return pets;
    }

    public void setPets(long pets)
    {
        this.pets = pets;
    }

    public Boolean getFemale()
    {
        return female;
    }

    public void setFemale(Boolean female)
    {
        this.female = female;
    }

    public java.sql.Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate)
    {
        this.startDate = startDate;
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
                        " Wealth: " + wealth +
                        " Staff: " + staff +
                        " Female: " + female+
                        " Start Date: "+startDate;
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
