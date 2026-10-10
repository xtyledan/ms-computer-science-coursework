package com.snhu.mscs.cs600.module2;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Tyler Daniels
 * Provides database persistence and retrieval for books.
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
}