package com.tsundokuplus.application.service

import com.tsundokuplus.domain.model.Email
import com.tsundokuplus.domain.model.Password
import com.tsundokuplus.domain.model.RoleType
import com.tsundokuplus.domain.model.User
import com.tsundokuplus.domain.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class UserServiceTest {
    private val userRepository = mock<UserRepository>()
    private val userService = UserService(userRepository)
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        user = User(
            1,
            Email("test@example.com"),
            Password.factory("password"),
            "Test User",
            RoleType.USER
        )
    }

    @Test
    fun `Get a user`() {
        val expected = user

        whenever(userRepository.findOne(user.email.value)).thenReturn(user)

        val result = userService.getUser(user.email.value)
        Assertions.assertThat(result).isEqualTo(expected)

        verify(userRepository).findOne(user.email.value)
    }

    @Test
    fun `Create a user`() {
        doNothing().`when`(userRepository).create(user)

        userService.createUser(user)

        verify(userRepository).create(user)
    }
}
