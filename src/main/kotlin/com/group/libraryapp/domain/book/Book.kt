package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book constructor(

    val name: String,

    @Convert(converter = BookTypeConvertor::class)
    val type: BookType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    init {
        require(name.isNotBlank()) { "이름은 비어 있을 수 없습니다" }
    }

    companion object{
        fun fixture(
            name: String = "default book name",
            type: BookType = BookType.UNCATEGORIZED,
            id: Long? = null
        ): Book {
            return Book(name, type, id)
        }
    }

}