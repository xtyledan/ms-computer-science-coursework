package com.snhu.mscs.cs600.salesguru;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository items;

    public ItemService(ItemRepository items) {
        this.items = items;
    }

    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return items.findAllByOrderByItemNumberAsc();
    }

    @Transactional(readOnly = true)
    public Item findById(Integer id) {
        return items.findById(id)
            .orElseThrow(() ->
                new NoSuchElementException("Item not found: " + id));
    }

    @Transactional
    public Item create(
            String description,
            BigDecimal cost,
            BigDecimal retail) {

        if (description == null ||
            description.isBlank() ||
            description.length() > 128) {

            throw new IllegalArgumentException(
                "Description must be between 1 and 128 characters.");
        }

        validateMoney(cost, "Cost");
        validateMoney(retail, "Retail price");

        Item item = new Item(
            description.trim(),
            cost,
            retail
        );

        return items.save(item);
    }

    private void validateMoney(BigDecimal value, String label) {

        if (value == null ||
            value.signum() < 0 ||
            value.scale() > 2 ||
            value.precision() - value.scale() > 10) {

            throw new IllegalArgumentException(
                label + " must be a nonnegative amount with at most two decimals.");
        }
    }
}