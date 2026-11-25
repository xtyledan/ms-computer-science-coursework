package com.snhu.mscs.cs600.salesguru;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTests {

    @Mock
    InvoiceRepository invoices;

    @Mock
    LineItemRepository lines;

    @Mock
    ItemRepository items;

    @Mock
    CompanyRepository companies;

    @Mock
    RepresentativeRepository representatives;

    private InvoiceService service;

    @BeforeEach
    void setUp() {

        service = new InvoiceService(
            invoices,
            lines,
            items,
            companies,
            representatives
        );
    }

    @Test
    void createsInvoiceWithCorrectDiscountedTotal() {

        Company company = new Company();
        company.setCustomer_number(1);
        company.setName("Sample Company");

        Representative representative = new Representative();
        representative.setRepresentative_id(7);
        representative.setCustomer_number(1);
        representative.setFirst("Alex");
        representative.setLast("Smith");

        Item product = new Item(
            "Example Widget",
            new BigDecimal("4.00"),
            new BigDecimal("10.00")
        );

        product.setItemNumber(3);

        when(companies.findById(1))
            .thenReturn(Optional.of(company));

        when(representatives.findById(7))
            .thenReturn(Optional.of(representative));

        when(items.findById(3))
            .thenReturn(Optional.of(product));

        when(invoices.save(any(Invoice.class)))
            .thenAnswer(invocation -> {

                Invoice saved = invocation.getArgument(0);
                saved.setInvoiceNumber(42);

                return saved;
            });

        Invoice persisted = new Invoice(
            1,
            7,
            LocalDate.now()
        );

        persisted.setInvoiceNumber(42);

        when(invoices.findById(42))
            .thenReturn(Optional.of(persisted));

        List<LineItem> persistedLines = new ArrayList<>();

        when(lines.save(any(LineItem.class)))
            .thenAnswer(invocation -> {

                LineItem saved = invocation.getArgument(0);
                saved.setLineItemId(5);

                persistedLines.add(saved);

                return saved;
            });

        when(lines.findByInvoiceNumberOrderByLineItemIdAsc(42))
            .thenAnswer(invocation -> persistedLines);

        InvoiceData.NewInvoice request =
            new InvoiceData.NewInvoice(
                1,
                7,
                LocalDate.now(),
                List.of(
                    new InvoiceData.NewLine(
                        3,
                        2,
                        new BigDecimal("10.00")
                    )
                )
            );

        InvoiceData.InvoiceView result =
            service.create(request);

        assertEquals(42, result.invoiceNumber());

        assertEquals(
            new BigDecimal("18.00"),
            result.total()
        );

        assertEquals(1, result.lines().size());

        assertEquals(
            new BigDecimal("10.00"),
            result.lines().get(0).unitPrice()
        );

        verify(invoices).save(any(Invoice.class));
    }

    @Test
    void rejectsRepresentativeFromAnotherCompany() {

        Company company = new Company();
        company.setCustomer_number(1);

        Representative representative = new Representative();
        representative.setCustomer_number(2);

        when(companies.findById(1))
            .thenReturn(Optional.of(company));

        when(representatives.findById(7))
            .thenReturn(Optional.of(representative));

        InvoiceData.NewInvoice request =
            new InvoiceData.NewInvoice(
                1,
                7,
                LocalDate.now(),
                List.of(
                    new InvoiceData.NewLine(
                        3,
                        1,
                        BigDecimal.ZERO
                    )
                )
            );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.create(request)
        );
    }

    @Test
    void rejectsInvoiceWithoutLines() {

        InvoiceData.NewInvoice request =
            new InvoiceData.NewInvoice(
                1,
                7,
                LocalDate.now(),
                List.of()
            );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.create(request)
        );
    }
}