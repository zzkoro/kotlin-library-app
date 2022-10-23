package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
open class BookServiceTest (
    @Autowired private val bookRepository: BookRepository,
    @Autowired private val bookService: BookService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("beforeAll")
        }

        @AfterAll()
        @JvmStatic
        fun afterAll() {
            println("afterAll")
        }
    }


    @AfterEach
    fun afterEach() {
        println("after each")
    }

    @Test
    @DisplayName("책 저장 테스트")
    @Transactional
    @Order(1)
    open fun saveBookTest() {
        // given
        val request = BookRequest("Alice Book")


        // when
        bookService.saveBook(request)

        // then
        val results = bookRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("Alice Book")
    }

    @Test
    @DisplayName("책 대출 테스트")
    @Transactional
    @Order(2)
    open fun loanBookTest() {
        // given
        bookRepository.save(Book("Alice Book"))
        userRepository.save(User("AAA", null))

        val bookLoanRequest = BookLoanRequest("AAA", "Alice Book")

        // when
        bookService.loanBook(bookLoanRequest)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("Alice Book")
        assertThat(results[0].user.name).isEqualTo("AAA")
        assertThat(results[0].isReturn).isFalse()
    }

    @Test
    @DisplayName("책 대출 실패 테스트 - 이미 대출되어 있는 책의 경우 ")
    @Transactional
    @Order(3)
    open fun loanBookFailTest() {
        // given
        val savedBook = bookRepository.save(Book("Alice Book"))
        val savedUser = userRepository.save(User("AAA", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, savedBook.name, false))

        val bookLoanRequest = BookLoanRequest("AAA", "Alice Book")

        // when & then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook((bookLoanRequest))
        }
    }

    @Test
    @Transactional
    @DisplayName("책 반납 테스트")
    @Order(4)
    open fun returnBookTest() {
        // given
        val savedBook = bookRepository.save(Book("Alice Book"))
        val savedUser = userRepository.save(User("AAA", null))

        val bookLoanRequest = BookLoanRequest("AAA", "Alice Book")
        bookService.loanBook(bookLoanRequest)

        val bookReturnRequest = BookReturnRequest("AAA", "Alice Book")

        // when
        bookService.returnBook(bookReturnRequest)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("Alice Book")
        assertThat(results[0].isReturn).isTrue()

    }

}