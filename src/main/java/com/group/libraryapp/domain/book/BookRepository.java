package com.group.libraryapp.domain.book;

import com.group.librayapp.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByName(String bookName);

}
