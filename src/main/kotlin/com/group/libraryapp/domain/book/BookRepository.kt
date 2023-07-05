package com.group.libraryapp.domain.book

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface BookRepository : JpaRepository<Book, Long>{
    // Book? -> service 계층의 호환성을 위한 Optional use
    fun findByName(bookName: String): Optional<Book>
}