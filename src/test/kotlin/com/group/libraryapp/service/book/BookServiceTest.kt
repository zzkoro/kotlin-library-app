package com.group.libraryapp.service.book

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
open class BookServiceTest(
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
        println("clean test data after each")
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 저장 테스트")
    @Order(1)
    open fun saveBookTest() {
        // given
        val request = BookRequest("Alice Book", BookType.COMPUTER)

        // when
        bookService.saveBook(request)

        // then
        val results = bookRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("Alice Book")
    }

    @Test
    @DisplayName("책 대출 테스트")
    @Order(2)
    open fun loanBookTest() {
        // given
        bookRepository.save(Book.fixture("Alice Book"))
        userRepository.save(User("AAA", null))

        val bookLoanRequest = BookLoanRequest("AAA", "Alice Book")

        // when
        bookService.loanBook(bookLoanRequest)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("Alice Book")
        assertThat(results[0].user.name).isEqualTo("AAA")
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    @DisplayName("책 대출 실패 테스트 - 이미 대출되어 있는 책의 경우 ")
    @Order(3)
    open fun loanBookFailTest() {
        // given
        val savedBook = bookRepository.save(Book.fixture("Alice Book"))
        val savedUser = userRepository.save(User("AAA", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, savedBook.name))

        val bookLoanRequest = BookLoanRequest("AAA", "Alice Book")

        // when & then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook((bookLoanRequest))
        }
    }

    @Test
    @DisplayName("책 반납 테스트")
    @Order(4)
    open fun returnBookTest() {
        // given
        val savedBook = bookRepository.save(Book.fixture("Alice Book"))
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
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName("책 대여 권수를 정상 확인한다.")
    fun countLoanedBookType() {
        // given
        var savedUser = userRepository.save(User("AAA", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "A"),
                UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(savedUser, "C", UserLoanStatus.RETURNED),
            )
        )

        // when
        val result = bookService.countLoanedBook()

        //then
        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야별 책 권수를 정상 확인한다.")
    fun getBookStatisticsTest() {
        // given
        bookRepository.saveAll(
            listOf(
                Book.fixture("A", BookType.COMPUTER),
                Book.fixture("B", BookType.COMPUTER),
                Book.fixture("C", BookType.SCIENCE),
            )
        )

        // when
        val results = bookService.getBookStatistics()

        //then
        assertThat(results).hasSize(2)
        val computerDto = results.first { result -> result.type == BookType.COMPUTER }
        assertThat(computerDto.count).isEqualTo(2)

        val scienceDto = results.first { result -> result.type == BookType.SCIENCE }
        assertThat(scienceDto.count).isEqualTo(1)
    }
}