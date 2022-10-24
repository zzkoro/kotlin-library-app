package com.group.libraryapp.service

import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.domain.book.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService2(
    private val bookRepository: BookRepository,
) {

    fun saveBook(request: BookRequest) {
    }
}