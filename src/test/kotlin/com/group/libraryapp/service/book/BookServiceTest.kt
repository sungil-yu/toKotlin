package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalArgumentException

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @AfterEach
    fun afterEach() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun saveBookTest() {
        val bookRequest = BookRequest("alice", BookType.UNCATEGORIZED)

        bookService.saveBook(bookRequest)

        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0]).extracting("name").isEqualTo("alice")
        assertThat(books[0].type).isEqualTo(BookType.UNCATEGORIZED)
    }

    @Test
    fun loadBookTest() {
        val book = bookRepository.save(Book.fixture("alice"))
        val user = userRepository.save(User("oncerun", null))
        val bookLoanRequest = BookLoanRequest(user.name, book.name)

        bookService.loanBook(bookLoanRequest)

        val result = userLoanHistoryRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo("alice")
        assertThat(result[0].user.id).isEqualTo(user.id)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @Test
    fun loadBookFailTest() {
        val book = bookRepository.save(Book.fixture("alice"))
        val user = userRepository.save(User("oncerun", null))
        val bookLoanRequest = BookLoanRequest(user.name, book.name)
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user, book.name))

        assertThrows<IllegalArgumentException> {
            bookService.loanBook(bookLoanRequest)
        }.apply {
            assertThat(this.message).isEqualTo("진작 대출되어 있는 책입니다")
        }

    }

    @Test
    fun returnBookTest() {
        val book = bookRepository.save(Book.fixture("alice"))
        val user = userRepository.save(User("oncerun", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(user, book.name))
        val bookReturnRequest = BookReturnRequest(user.name, book.name)

        bookService.returnBook(bookReturnRequest)

        val results = userLoanHistoryRepository.findAll()

        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    fun checkLoanedBookCount() {
        val user = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(user, "A"),
                UserLoanHistory.fixture(user, "B", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(user, "C", UserLoanStatus.RETURNED)
            )
        )

        val result = bookService.countLoadedBook()

        assertThat(result).isEqualTo(1)
    }
    @Test
    @DisplayName("Verify that the stats for books by category are accurate.")
    fun getBookStatistics() {
        bookRepository.saveAll(listOf(
            Book.fixture("A", BookType.COMPUTER),
            Book.fixture("B", BookType.COMPUTER),
            Book.fixture("A", BookType.SCIENCE)
        ))

        val results = bookService.getBookStatistics()
        assertThat(results).hasSize(2)

        val computers = results.first {it.type == BookType.COMPUTER}
        assertThat(computers.count).isEqualTo(2L)

        val science = results.first { it.type == BookType.SCIENCE}
        assertThat(science.count).isEqualTo(1L)
    }
}