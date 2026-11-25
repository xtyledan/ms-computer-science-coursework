package com.snhu.mscs.cs600.salesguru;

/**
 * Imports to support the record that will be obtained from the Database. 
 */
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Representative - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the representation of a single
 *      record as it exists in the database. This is considered an 'Entity'
 *      class because it defines the object that represents the database record.
 *      Complex applications may have more than one Entity class for interacting
 *      with the database through the Spring Data JPA framework.
 * 
 *      This specific class maps to the Representative table in the SalesGuru
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
public class Representative {

    /**
     * representative_id - The unique id (and Primary key) for the data record. 
     *      This uniquely identifies each representative in our database.
     * 
     *      This validates requirement #2, The application shall provide a 
     *      mechanism to assign a unique representative ID to each 
     *      representative for a company.
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
    private int representative_id;
    
    /**
     * customer_number - Foreign Key used to map representatives to companies.
     */
    private int customer_number;

    /**
     * first - The first name of the representative.
     */
    private String first;
    
    /**
     * middle - The middle name of the representative.
     */
    private String middle;

    /**
     * last - The last name of the representative.
     */
    private String last;

    /**
     * suffix - Any potentional naming suffix for the representative.
     */
    private String suffix;

    /**
     * salutation - The preferred address for the representative.
     */
    private String salutation;

    /**
     * direct_line - The phone number for the representative's desk.
     */
    private String direct_line;

    /**
     * mobile - The cel phone number for the representative.
     */
    private String mobile;

    /**
     * The following section includes the default (empty) constructor as well as
     * accessor and mutator methods to provide encapsulation for each of the four 
     * data attributes. Comments have not been provided as these are trivial.
     */
    public int getRepresentative_id() {
        return representative_id;
    }

    public void setRepresentative_id(int representative_id) {
        this.representative_id = representative_id;
    }

    public int getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(int customer_number) {
        this.customer_number = customer_number;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getDirect_line() {
        return direct_line;
    }

    public void setDirect_line(String direct_line) {
        this.direct_line = direct_line;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
}
