package com.tsundokuplus.domain.model

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@JvmInline
value class Password(val value: String) {
    init {
        require(!BCryptPasswordEncoder().matches("", value))
    }

    companion object {
        private fun hash(rawPassword: String): String {
            return BCryptPasswordEncoder().encode(rawPassword)
        }

        fun factory(rawPassword: String): Password {
            return Password(this.hash(rawPassword))
        }
    }
}
