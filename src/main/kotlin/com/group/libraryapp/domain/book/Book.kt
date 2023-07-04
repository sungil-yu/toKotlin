package com.group.libraryapp.domain.book

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Book (
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // default parameter 맨 밑에 있는 것이 관례
) {

    init {
        require(name.isNotBlank()) { "이름은 비어 있을 수 없습니다" }
    }

}