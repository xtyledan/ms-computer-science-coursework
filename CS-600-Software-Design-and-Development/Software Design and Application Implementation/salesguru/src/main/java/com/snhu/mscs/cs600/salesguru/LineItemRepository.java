package com.snhu.mscs.cs600.salesguru;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemRepository
        extends JpaRepository<LineItem, Integer> {

    List<LineItem> findByInvoiceNumberOrderByLineItemIdAsc(
        Integer invoiceNumber
    );
}