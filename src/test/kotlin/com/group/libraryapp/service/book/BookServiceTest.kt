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
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
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
        val bookRequest = BookRequest("alice")

        bookService.saveBook(bookRequest)

        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0]).extracting("name").isEqualTo("alice")
    }

    @Test
    fun loadBookTest() {
        val book = bookRepository.save(Book("alice"))
        val user = userRepository.save(User("oncerun", null))
        val bookLoanRequest = BookLoanRequest(user.name, book.name)

        bookService.loanBook(bookLoanRequest)


        val result = userLoanHistoryRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo("alice")
        assertThat(result[0].user.id).isEqualTo(user.id)
        assertThat(result[0].isReturn).isFalse
    }

    @Test
    fun loadBookFailTest() {
        val book = bookRepository.save(Book("alice"))
        val user = userRepository.save(User("oncerun", null))
        val bookLoanRequest = BookLoanRequest(user.name, book.name)
        userLoanHistoryRepository.save(UserLoanHistory(user, book.name, false))



        assertThrows<IllegalArgumentException> {
            bookService.loanBook(bookLoanRequest)
        }.apply {
            assertThat(this.message).isEqualTo("진작 대출되어 있는 책입니다")
        }

    }

    @Test
    fun returnBookTest() {
        val book = bookRepository.save(Book("alice"))
        val user = userRepository.save(User("oncerun", null))
        userLoanHistoryRepository.save(UserLoanHistory(user, book.name, false))
        val bookReturnRequest = BookReturnRequest(user.name, book.name)

        bookService.returnBook(bookReturnRequest)

        val results = userLoanHistoryRepository.findAll()

        assertThat(results).hasSize(1)
        assertThat(results[0].isReturn).isTrue
    }
}