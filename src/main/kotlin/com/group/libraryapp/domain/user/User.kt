package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User constructor(

    var name:String,
    val age:Int?,

    //kotlin annotation 속성의 배열속성에는 대괄호 필요 []
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    init {
        require(name.isNotEmpty()) { "이름은 비어 있을 수 없습니다" }
    }

    fun updateName(name: String){
        this.name = name
    }

    fun loanBook(book: Book){
        this.userLoanHistories.add(UserLoanHistory(this, book.name))
    }

    fun returnBook(bookName: String){
        this.userLoanHistories.first { history -> history.bookName == bookName}.doReturn()
    }


}