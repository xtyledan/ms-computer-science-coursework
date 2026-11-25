package com.snhu.mscs.cs600.salesguru;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTests {

    @Mock
    ItemRepository repository;

    private ItemService service;

    @BeforeEach
    void setUp() {
        service = new ItemService(repository);
    }

    @Test
    void savesValidProduct() {

        service.create(
            "Widget",
            new BigDecimal("1.99"),
            new BigDecimal("4.99")
        );

        verify(repository).save(
            org.mockito.ArgumentMatchers.any(Item.class)
        );
    }

    @Test
    void rejectsNegativePrice() {

        assertThrows(
            IllegalArgumentException.class,
            () -> service.create(
                "Widget",
                new BigDecimal("-1.00"),
                BigDecimal.TEN
            )
        );
    }

    @Test
    void rejectsBlankDescription() {

        assertThrows(
            IllegalArgumentException.class,
            () -> service.create(
                "  ",
                BigDecimal.ZERO,
                BigDecimal.TEN
            )
        );
    }
}