package com.snhu.mscs.cs600.salesguru;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SalesGuruPlusApiController {

    private final ItemService items;
    private final InvoiceService invoices;

    public SalesGuruPlusApiController(
            ItemService items,
            InvoiceService invoices) {

        this.items = items;
        this.invoices = invoices;
    }

    public record NewItem(
        @NotBlank
        @Size(max = 128)
        String description,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal cost,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal retail
    ) {}

    @GetMapping("/items")
    public List<Item> listItems() {
        return items.findAll();
    }

    @PostMapping("/items")
    public ResponseEntity<Item> addItem(
            @Validated @RequestBody NewItem request) {

        Item saved = items.create(
            request.description(),
            request.cost(),
            request.retail()
        );

        return ResponseEntity
            .created(URI.create(
                "/api/v1/items/" + saved.getItemNumber()))
            .body(saved);
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable Integer id) {
        return items.findById(id);
    }

    @GetMapping("/invoices")
    public List<InvoiceData.InvoiceView> listInvoices() {
        return invoices.findAll();
    }

    @GetMapping("/invoices/{id}")
    public InvoiceData.InvoiceView getInvoice(
            @PathVariable Integer id) {

        return invoices.get(id);
    }

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceData.InvoiceView> addInvoice(
            @Valid @RequestBody InvoiceData.NewInvoice request) {

        InvoiceData.InvoiceView saved = invoices.create(request);

        return ResponseEntity
            .created(URI.create(
                "/api/v1/invoices/" + saved.invoiceNumber()))
            .body(saved);
    }
}