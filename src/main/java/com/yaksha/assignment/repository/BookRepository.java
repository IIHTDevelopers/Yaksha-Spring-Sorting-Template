package com.yaksha.assignment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yaksha.assignment.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	// Query method to find books by title with pagination and sorting
	Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

	// Custom JPQL query to find books by rating above a threshold with pagination
	// and sorting
	@Query("SELECT b FROM Book b WHERE b.rating > :rating")
	Page<Book> findBooksByRatingAbove(double rating, Pageable pageable);

	// Custom query method to find books by author with pagination and sorting
	Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

	// This is for returning all books with pagination and sorting
	@Override
	Page<Book> findAll(Pageable pageable);
}
