package com.snhu.mscs.cs600.salesguru;

/**
 * Imports to support the record that will be obtained from the Database. 
 */
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Company - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the representation of a single
 *      record as it exists in the database. This is considered an 'Entity'
 *      class because it defines the object that represents the database record.
 *      Complex applications may have more than one Entity class for interacting
 *      with the database through the Spring Data JPA framework.
 * 
 *      This specific class maps to the Company table in the SalesGuru
 *      database.
 *
 * Class Level Annotations:
 *      @Entity - This tells the Spring framework that this class defines an
 *      object that will be used to represent a record from the database 
 *      connected through Spring Data JPA.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used to represent data from and persist data to the
 *      database connected through the Spring Data JPA framework.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        7-Oct-2025     Initial Creation
 */
@Entity
public class Company {
    
    /**
     * customer_number - The unique id (and Primary key) for the data record. 
     *      This uniquely identifies each company in our database.
     * 
     *      This validates requirement #1, The application shall provide a 
     *      mechanism to ensure that each company has a unique customer number.
     * 
     * Annotations:
     *      @Id - indicates to the framework that this variable represents the
     *      primary key used by the data record.
     *      @GeneratedValue - indicates to the framework that when a new object
     *      is created by the application, the framework will assign it a unique
     *      value. The strategy selected here (GenerationType.SEQUENCE) uses 
     *      the sequence object that has already been created in the database
     *      to obtain the next value.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int customer_number;

    /**
     * name - The name of the company that is our customer.
     */
    private String name;

    /**
     * addr1 - The first line of the company's address.
     */
    private String addr1;

    /**
     * addr2 - The second line of the company's address.
     */
    private String addr2;

    /**
     * city - The city within which the company is located.
     */
    private String city;

    /**
     * state - The state within which the company is located.
     */
    private String state;

    /**
     * zip - The company's zipcode.
     */
    private String zip;

    /**
     * The following section includes the default (empty) constructor as well as
     * accessor and mutator methods to provide encapsulation for each of the four 
     * data attributes. Comments have not been provided as these are trivial.
     */

    public Company()
    {

    }

    public int getCustomer_number() {
        return customer_number;
    }
    
    public void setCustomer_number(int customer_number) {
        this.customer_number = customer_number;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddr1() {
        return addr1;
    }
    
    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }
    
    public String getAddr2() {
        return addr2;
    }
    
    public void setAddr2(String addr2) {
        this.addr2 = addr2;
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
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
