package com.tsundokuplus.domain.repository

import com.tsundokuplus.domain.model.user.User

interface UserRepository {
    fun findOne(email: String): User?
    fun create(user: User)
}
