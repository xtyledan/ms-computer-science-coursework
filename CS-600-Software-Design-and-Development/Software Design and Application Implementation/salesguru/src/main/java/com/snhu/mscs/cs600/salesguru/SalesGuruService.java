package com.snhu.mscs.cs600.salesguru;

/**
 * Imports to support Spring JPA configuration
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Imports to provide access to Java Collections
 */
import java.util.List;

/**
 * SalesGuruService - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the necessary functionality
 *      to interact with the Repository interfaces for Spring Data JPA. This is
 *      the class that interacts with the application (or API controller(s))
 *      to provide access to a database.
 *
 *      This class is currently configured to interface with the following
 *      repositories:
 *      
 *          CompanyRepository
 *          RepresentativeRepository
 * 
 * Class Level Annotations:
 *      @Service - This tells the spring framework that this
 *      class is a Service implementation and enables the appropriate capabilities
 *      through the framework.
 * 
 * Sample Output:
 *      This service utilizes the queries that are enabled in the Repository
 *      interface in order to return data to our application - in this case - 
 *      the APIController
 * 
 * Constraints: N/A
 * 
 * Expected Results:
 *      Each query executed in the class will provide appropriate data as
 *      necessary to the calling function within the application.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        3-Oct-2025     Initial Creation
 */
@Service
public class SalesGuruService {
    
    @Autowired 
    private CompanyRepository companyRepo;

    @Autowired
    private RepresentativeRepository representativeRepo;

   
    public SalesGuruService(CompanyRepository cr, RepresentativeRepository rr)
    {
        this.companyRepo = cr;
        this.representativeRepo = rr;
    }

    public List<Company> listCompanies()
    {
        return companyRepo.listCompanies();
    }

    public List<Representative> listRepresentatives()
    {
        return representativeRepo.listRepresentatives();
    }

    public List<Representative> listRepresentativesForCompany(int customer_number)
    {
        return representativeRepo.listRepresentativesForCompany(customer_number);
    }
}
