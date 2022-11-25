package com.tsundokuplus.application.service

import com.tsundokuplus.domain.model.User
import com.tsundokuplus.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun getUser(email: String): User? {
        return userRepository.findOne(email)
    }
}
