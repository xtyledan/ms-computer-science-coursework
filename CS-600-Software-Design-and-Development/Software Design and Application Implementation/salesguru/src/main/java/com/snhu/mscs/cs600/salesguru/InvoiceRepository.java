package com.snhu.mscs.cs600.salesguru;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Integer> {

    List<Invoice> findAllByOrderByInvoiceNumberDesc();

    List<Invoice> findByCustomerNumberOrderByInvoiceNumberDesc(
        Integer customerNumber
    );
}