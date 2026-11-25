package com.snhu.mscs.cs600.salesguru;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class InvoiceData {

    private InvoiceData() {}

    public record NewLine(
        @NotNull
        @Positive
        Integer itemNumber,

        @NotNull
        @Positive
        Integer quantity,

        @NotNull
        @DecimalMin("0.00")
        @DecimalMax("100.00")
        BigDecimal discountPercent
    ) {}

    public record NewInvoice(
        @NotNull
        @Positive
        Integer customerNumber,

        @NotNull
        @Positive
        Integer representativeId,

        @NotNull
        LocalDate dateOfSale,

        @NotEmpty
        List<@Valid NewLine> lines
    ) {}

    public record LineView(
        Integer lineItemId,
        Integer itemNumber,
        String description,
        Integer quantity,
        BigDecimal discountPercent,
        BigDecimal unitPrice,
        BigDecimal lineTotal
    ) {}

    public record InvoiceView(
        Integer invoiceNumber,
        Integer customerNumber,
        String companyName,
        Integer representativeId,
        String representativeName,
        LocalDate dateOfSale,
        List<LineView> lines,
        BigDecimal total
    ) {}
}