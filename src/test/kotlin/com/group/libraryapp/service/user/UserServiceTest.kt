package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
open class UserServiceTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userService: UserService,
    @Autowired private val userLoanHistoryRepository: UserLoanHistoryRepository,

) {

    @AfterEach
    fun afterEach() {
        println("clean test data after each")
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("사용자 저장 테스트")
    open fun saveUserTest() {
        // given
        val request = UserCreateRequest("aaa", null)

        // when
        userService.saveUser(request)

        // then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("aaa")
        assertThat(results[0].age).isNull()
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    open fun geUsersTest() {
        // given
        userRepository.saveAll(
            listOf(
                User("A", 20),
                User("B", null)
            )
        )

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    open fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
    fun getUserLoanHistoriesTest1() {
        // given
        userRepository.save(User("A", null))

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다.")
    fun getUserLoanHistoriesTest2() {
        // given
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "B1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "B2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "B3", UserLoanStatus.RETURNED),
            )
        )

        val savedUser2 = userRepository.save(User("B", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser2, "B1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser2, "B2", UserLoanStatus.LOANED),
            )
        )

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(2)
        val userResults = results.first { it.name == "A" }
        assertThat(userResults.name).isEqualTo("A")
        assertThat(userResults.books).hasSize(3)
        assertThat(userResults.books).extracting(("name")).containsExactlyInAnyOrder("B1", "B2", "B3")
    }
}