package com.snhu.mscs.cs600.salesguru;

// Imports to support API Development
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Import java collection capabilities
import java.util.List;

/**
 * APIController - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the necessary controller
 *      mappings for a RESTful API. These will utilize additional classes
 *      and libraries as necessary to create and maintain the necessary
 *      API functionality for the Sales Guru Application.
 * 
 * Class Level Annotations:
 *      @RestController - This tells the spring framework that this
 *      class is providing RESTful API endpoints for the application
 *      and enables those components in the framework.
 * 
 *      @RequestMapping - This tells the spring framework what the 
 *      root of the namespace is for our RESTful API.
 * 
 * Sample Output:
 *      Accessing the 'version' endpoint provided by this application
 *      through the URL http://127.0.0.1:8080/api/v1/version should
 *      result in:
 *              Version-1.0
 * 
 * Constraints: N/A
 * 
 * Expected Results:
 *      Each API endpoint in this class should respond as defined and
 *      documented to appropriely formatted calls.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        3-Oct-2025     Initial Creation
 */
@RestController
@RequestMapping("/api/v1")
public class APIController {
    
    @Autowired
    private SalesGuruService salesGuruService;

    /**
     * APIController - This is a generic placeholder for the 
     * constructor for this class. Its purpose is to provide a placeholder
     * as future development on this application may require adaptations
     * to the constructor.
     */
    public APIController(SalesGuruService service){
        this.salesGuruService = service;
    }

    /**
     * getVersion - This method supports a simple return of what version
     * of the API is being provided. This should be changed when major
     * changes are made to this API.
     * 
     * Annotations:
     *      @GetMapping - This annotation indicates the name of the 
     *      endpoint. It is added to the end of the value of 
     *      @RequestMapping that sets the namespace for this class.
     * 
     * @return String indicating the version of the API being provided.
     */
    @GetMapping("/version")
    public String getVersion(){
        return "Version-1.0";
    }

    /**
     * companies - This method supports the display of a list of companies.
     * This version of the /companies endpoint that calls for the data
     * directly from the Database.
     * 
     * This endpoint provides validation for requirement #7 - "The application
     * shall provide a mechanism to display all companies in the system."
     * 
     * Annotations:
     *      @GetMapping - This annotation indicates the name of the 
     *      endpoint. It is added to the end of the value of 
     *      @RequestMapping that sets the namespace for this class.
     * 
     * @return A JSON structure (as a String) containing the list of
     *         company objects from the database.
     */
    @GetMapping("/companies")
    public List<Company> listCompanies()
    {
        return salesGuruService.listCompanies();
    }

    /**
     * representatives - This method supports the display of a list of 
     * representatives from the database. This only lists the representative
     * data, it does not map them to companies.
     * 
     * Annotations:
     *      @GetMapping - This annotation indicates the name of the 
     *      endpoint. It is added to the end of the value of 
     *      @RequestMapping that sets the namespace for this class.
     * 
     * @return A JSON structure (as a String) containing the list of
     *         representative objects from the database.
     */
    @GetMapping("/representatives")
    public List<Representative> listRepresentatives()
    {
        return salesGuruService.listRepresentatives();
    }

    /**
     * repsForCompany/customer_number - This method supports the display of a list of 
     * representatives from the database that represent the company identified by 
     * the customer_number. 
     * 
     * This endpoint verifies requirement #6 - "The application shall provide a mechanism
     * to display all representatives for a given company." and correspondingly reqiurement
     * #3 - "The application shall provide a mechanism to link representatives to a single
     * company." (this is done through the customer_numnber foreign key in the representatives
     * table in the database and displayed here through the parameterized RESTful endpoint).
     * 
     * Annotations:
     *      @GetMapping - This annotation indicates the name of the 
     *      endpoint. It is added to the end of the value of 
     *      @RequestMapping that sets the namespace for this class.
     *      @PathVariable - This annotation is a means of accessing the data
     *          the client is sending to the RESTful interface. In this case it is the
     *          customer_number used as a filter for the database query.
     *
     * @param int representing the customer_number used to filter the list
     *          of representatives from the database.
     * @return A JSON structure (as a String) containing the list of
     *         representative objects from the database.
     */
    @GetMapping("/repsForCompany/{customer_number}")
    public List<Representative> listRepresentativesForCompany(@PathVariable int customer_number)
    {
        return salesGuruService.listRepresentativesForCompany(customer_number);
    }
}
