package com.group.libraryapp.domain.book

import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class BookTypeConvertor : AttributeConverter<BookType, String>{

    override fun convertToDatabaseColumn(bookType: BookType?): String? {
        if(bookType == null) return null;
        return bookType.value
    }

    override fun convertToEntityAttribute(dbData: String?): BookType? {
        if(dbData == null) return null
        return BookType.fromValue(dbData)
    }

}