package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.util.fail
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
     private val userRepository: UserRepository,
     private val userLoanHistoryRepository: UserLoanHistoryRepository,
     private val userService: UserService
) {

    @AfterEach
    fun beforeEach() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUserTest() {
        val request = UserCreateRequest("oncerun", null)

        userService.saveUser(request)

        val user = userRepository.findByName("oncerun") ?: fail()

        assertThat(user.name).isEqualTo("oncerun")
        assertThat(user.age).isNull()
    }


    @Test
    fun getUsersTest() {
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null),
        ))

        val users = userService.getUsers()

        assertThat(users).hasSize(2)
        assertThat(users).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(users).extracting("age").containsExactlyInAnyOrder(20, null)
    }


    @Test
    fun updateUserNameTest() {
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id!!, "B")

        userService.updateUserName(request)

        val updatedUser = userRepository.findAll()[0]

        assertThat(updatedUser.name).isEqualTo("B")
    }

    @Test
    fun deleteUserTest() {
        userRepository.save(User("A", null))

        userService.deleteUser("A")

        assertThat(userRepository.findAll()).isEmpty()
    }


    @Test
    fun getUserAllWithLoans() {
        userRepository.save(User("A", null))

        val results = userService.getUserLoanHistories()

        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    fun getUserWithLoanHistories() {
        val user = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(user, "book1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(user, "book2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(user, "book3", UserLoanStatus.RETURNED)
            )
        )

        var results = userService.getUserLoanHistories()

        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name")
            .containsExactlyInAnyOrder("book1", "book2", "book3")
        assertThat(results[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }
}