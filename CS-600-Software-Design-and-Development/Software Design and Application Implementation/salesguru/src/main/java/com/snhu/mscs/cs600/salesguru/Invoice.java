package com.snhu.mscs.cs600.salesguru;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_number")
    private Integer invoiceNumber;

    @Column(name = "customer_number", nullable = false)
    private Integer customerNumber;

    @Column(name = "representative_id", nullable = false)
    private Integer representativeId;

    @Column(name = "date_of_sale", nullable = false)
    private LocalDate dateOfSale;

    public Invoice() {}

    public Invoice(
            Integer customerNumber,
            Integer representativeId,
            LocalDate dateOfSale) {

        this.customerNumber = customerNumber;
        this.representativeId = representativeId;
        this.dateOfSale = dateOfSale;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Integer getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Integer representativeId) {
        this.representativeId = representativeId;
    }

    public LocalDate getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDate dateOfSale) {
        this.dateOfSale = dateOfSale;
    }
}