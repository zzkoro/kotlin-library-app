package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
open class UserServiceTest (
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userService: UserService
) {


    @AfterEach
    fun afterEach() {
        println("after each")
    }

    @Test
    @DisplayName("사용자 저장 테스트")
    @Transactional
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
    @Transactional
    open fun geUsersTest() {
        // given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null)
        ))

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)


    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    @Transactional
    open fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }

}