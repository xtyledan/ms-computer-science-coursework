package com.snhu.mscs.cs600.salesguru;

/**
 * Imports for Spring JPA Compatibility and annotations.
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Import for the List collection.
 */
import java.util.List;

/**
 * RepresentativeRepository - 
 * 
 * Purpose:
 *      The purpose of this interface is to provide the necessary functionality
 *      to interact with the representative table within the Postgres Database.
 *
 *      The Spring Data JPA framework creates the CRUD implementation 
 *      automatically at runtime
 * 
 * Class Level Annotations:
 *      @Repository - This tells the spring framework that this
 *      class is a Repository interface and enables access through the Spring JPA
 *      framework.
 * 
 * Sample Output:
 *      None - directly. However, the queries that are enabled in the Repository
 *      interface are used in the Service (NameService) class to return
 *      data to the APIController
 * 
 * Constraints: N/A
 * 
 * Expected Results:
 *      Each query executed in the class will provide appropriate data to 
 *      the corresponding Service function for access by the API controller.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        6-Oct-2025     Initial Creation
 */
@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Integer>{
    
    /**
     * listRepresentatives - Find all representatives in the database.
     * 
     * Annotations:
     *      @Query - String representing the appropriate query for the
     *      attached database. This can use either JPQL or native SQL.
     *      This query is configured to use native SQL for Postgres.
     * 
     * @return A collection of Company names from the database.
     */
    @Query(nativeQuery = true, value = "SELECT customer_number, first, middle, last, suffix, salutation, direct_line, mobile, representative_id FROM representative")
    List<Representative> listRepresentatives();
    
    /**
     * listRepresentativesForCompany - Find all representatives 
     * for a specific company in the database.
     * 
     * Annotations:
     *      @Query - String representing the appropriate query for the
     *      attached database. This can use either JPQL or native SQL.
     *      This query is configured to use native SQL for Postgres.
     * 
     * @param int representing the customer_number of the company used as a search
     *          criteria to filter the list of representatives in the database.
     * @return A collection of Company names from the database.
     */
    @Query(nativeQuery = true, value = "SELECT customer_number, first, middle, last, suffix, salutation, direct_line, mobile, representative_id FROM representative where customer_number = :search")
    List<Representative> listRepresentativesForCompany(@Param("search") int customer_number);

}
