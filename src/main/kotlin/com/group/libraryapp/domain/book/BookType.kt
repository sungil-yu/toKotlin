package com.group.libraryapp.domain.book

import java.lang.IllegalArgumentException

enum class BookType constructor(
    val value: String
) {
    SCIENCE("SCIENCE"),
    SOCIETY("SOCIETY"),
    ECONOMY("ECONOMY"),
    COMPUTER("COMPUTER"),
    UNCATEGORIZED("UNCATEGORIZED"),
    LANGUAGE("LANGUAGE");

    companion object {
        fun fromValue(value: String): BookType {
            return values().firstOrNull {
                it.value == value
            } ?: throw IllegalArgumentException("Format $value is illegal")
        }
    }
}