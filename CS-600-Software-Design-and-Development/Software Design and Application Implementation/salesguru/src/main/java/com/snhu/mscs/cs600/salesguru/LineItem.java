package com.snhu.mscs.cs600.salesguru;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "line_items")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_item_id")
    private Integer lineItemId;

    @Column(name = "invoice_number", nullable = false)
    private Integer invoiceNumber;

    @Column(name = "item_number", nullable = false)
    private Integer itemNumber;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(
        name = "discount",
        nullable = false,
        precision = 5,
        scale = 2
    )
    private BigDecimal discount;

    @Column(
        name = "unit_price",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal unitPrice;

    public LineItem() {}

    public LineItem(
            Integer invoiceNumber,
            Integer itemNumber,
            Integer qty,
            BigDecimal discount,
            BigDecimal unitPrice) {

        this.invoiceNumber = invoiceNumber;
        this.itemNumber = itemNumber;
        this.qty = qty;
        this.discount = discount;
        this.unitPrice = unitPrice;
    }

    public Integer getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Integer lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}