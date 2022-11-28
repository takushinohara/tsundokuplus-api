package com.tsundokuplus.domain.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    fun `Email can't be empty`() {
        Assertions.assertThatIllegalArgumentException().isThrownBy {
            User(
                email = Email(""),
                password = Password.factory("password"),
                name = "Test User"
            )
        }.withMessage("Failed requirement.")
    }

    @Test
    fun `Password can't be empty`() {
        Assertions.assertThatIllegalArgumentException().isThrownBy {
            User(
                email = Email("test@example.com"),
                password = Password.factory(""),
                name = "Test User"
            )
        }.withMessage("Failed requirement.")
    }
}
