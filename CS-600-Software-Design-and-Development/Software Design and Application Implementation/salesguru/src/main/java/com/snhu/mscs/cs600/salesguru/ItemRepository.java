package com.snhu.mscs.cs600.salesguru;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOrderByItemNumberAsc();
}