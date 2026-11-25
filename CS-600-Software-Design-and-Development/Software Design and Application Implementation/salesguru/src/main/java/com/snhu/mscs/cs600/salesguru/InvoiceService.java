
package com.snhu.mscs.cs600.salesguru;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private static final BigDecimal ONE_HUNDRED =
        new BigDecimal("100");

    private static final BigDecimal ONE = BigDecimal.ONE;

    private final InvoiceRepository invoices;
    private final LineItemRepository lines;
    private final ItemRepository items;
    private final CompanyRepository companies;
    private final RepresentativeRepository representatives;

    public InvoiceService(
            InvoiceRepository invoices,
            LineItemRepository lines,
            ItemRepository items,
            CompanyRepository companies,
            RepresentativeRepository representatives) {

        this.invoices = invoices;
        this.lines = lines;
        this.items = items;
        this.companies = companies;
        this.representatives = representatives;
    }

    @Transactional
    public InvoiceData.InvoiceView create(
            InvoiceData.NewInvoice request) {

        if (request == null ||
            request.customerNumber() == null ||
            request.representativeId() == null ||
            request.dateOfSale() == null ||
            request.lines() == null ||
            request.lines().isEmpty()) {

            throw new IllegalArgumentException(
                "Company, representative, date and line items are required.");
        }

        if (request.dateOfSale().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException(
                "Sale date cannot be in the future.");
        }

        Company company = companies
            .findById(request.customerNumber())
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Company does not exist."));

        Representative representative = representatives
            .findById(request.representativeId())
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Representative does not exist."));

        if (representative.getCustomer_number()
                != company.getCustomer_number()) {

            throw new IllegalArgumentException(
                "Representative must belong to the selected company.");
        }

        List<Item> catalogItems = new ArrayList<>();

        for (InvoiceData.NewLine entry : request.lines()) {

            if (entry == null ||
                entry.itemNumber() == null ||
                entry.quantity() == null ||
                entry.quantity() <= 0 ||
                entry.discountPercent() == null ||
                entry.discountPercent().signum() < 0 ||
                entry.discountPercent().compareTo(ONE_HUNDRED) > 0 ||
                entry.discountPercent().scale() > 2) {

                throw new IllegalArgumentException(
                    "Each line requires a valid item, quantity and discount.");
            }

            Item item = items.findById(entry.itemNumber())
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Item does not exist: " + entry.itemNumber()));

            if (item.getRetail() == null ||
                item.getRetail().signum() < 0) {

                throw new IllegalArgumentException(
                    "Item must have a valid retail price.");
            }

            catalogItems.add(item);
        }

        Invoice invoice = new Invoice(
            request.customerNumber(),
            request.representativeId(),
            request.dateOfSale()
        );

        Invoice savedInvoice = invoices.save(invoice);

        for (int i = 0; i < request.lines().size(); i++) {

            InvoiceData.NewLine entry = request.lines().get(i);
            Item item = catalogItems.get(i);

            // Convert percentage input into fractional storage.
            // Example: 10 becomes 0.10.
            BigDecimal discountFraction =
                entry.discountPercent()
                    .divide(
                        ONE_HUNDRED,
                        6,
                        RoundingMode.HALF_UP
                    );

            LineItem line = new LineItem(
                savedInvoice.getInvoiceNumber(),
                item.getItemNumber(),
                entry.quantity(),
                discountFraction,
                item.getRetail()
            );

            lines.save(line);
        }

        return get(savedInvoice.getInvoiceNumber());
    }

    @Transactional(readOnly = true)
    public List<InvoiceData.InvoiceView> findAll() {

        return invoices.findAllByOrderByInvoiceNumberDesc()
            .stream()
            .map(this::toView)
            .toList();
    }

    @Transactional(readOnly = true)
    public InvoiceData.InvoiceView get(Integer id) {

        Invoice invoice = invoices.findById(id)
            .orElseThrow(() ->
                new NoSuchElementException(
                    "Invoice not found: " + id));

        return toView(invoice);
    }

    private InvoiceData.InvoiceView toView(Invoice invoice) {

        String companyName = companies
            .findById(invoice.getCustomerNumber())
            .map(Company::getName)
            .orElse("Unknown company");

        String representativeName = representatives
            .findById(invoice.getRepresentativeId())
            .map(rep ->
                (rep.getFirst() + " " + rep.getLast()).trim())
            .orElse("Unknown representative");

        List<InvoiceData.LineView> views = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO.setScale(2);

        List<LineItem> invoiceLines =
            lines.findByInvoiceNumberOrderByLineItemIdAsc(
                invoice.getInvoiceNumber());

        for (LineItem line : invoiceLines) {

            String description = items
                .findById(line.getItemNumber())
                .map(Item::getDescription)
                .orElse("Unavailable product");

            if (line.getUnitPrice() == null ||
                line.getQty() == null ||
                line.getQty() <= 0) {

                throw new IllegalStateException(
                    "Invoice contains an invalid line item: " +
                    line.getLineItemId());
            }

            BigDecimal discountFraction =
                line.getDiscount() == null
                    ? BigDecimal.ZERO
                    : line.getDiscount();

            if (discountFraction.signum() < 0 ||
                discountFraction.compareTo(ONE) > 0) {

                throw new IllegalStateException(
                    "Invalid stored discount for line item: " +
                    line.getLineItemId());
            }

            // Calculate subtotal before applying discount.
            BigDecimal subtotal = line.getUnitPrice()
                .multiply(BigDecimal.valueOf(line.getQty()));

            // Stored discounts use fractions.
            // 0.10 = 10%, 0.20 = 20%, 0.05 = 5%.
            BigDecimal afterDiscount = subtotal
                .multiply(ONE.subtract(discountFraction))
                .setScale(2, RoundingMode.HALF_UP);

            total = total.add(afterDiscount);

            // Convert the stored fraction into a percentage.
            BigDecimal displayDiscount = discountFraction
                .multiply(ONE_HUNDRED)
                .stripTrailingZeros();

            // Prevent scientific notation such as 1E+1.
            if (displayDiscount.scale() < 0) {
                displayDiscount = displayDiscount.setScale(0);
            }

            views.add(new InvoiceData.LineView(
                line.getLineItemId(),
                line.getItemNumber(),
                description,
                line.getQty(),
                displayDiscount,
                line.getUnitPrice(),
                afterDiscount
            ));
        }

        return new InvoiceData.InvoiceView(
            invoice.getInvoiceNumber(),
            invoice.getCustomerNumber(),
            companyName,
            invoice.getRepresentativeId(),
            representativeName,
            invoice.getDateOfSale(),
            views,
            total.setScale(2, RoundingMode.HALF_UP)
        );
    }
}
