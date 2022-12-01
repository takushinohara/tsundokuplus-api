package com.tsundokuplus.application.controller

import com.tsundokuplus.application.service.UserService
import com.tsundokuplus.application.service.security.LoginUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class AuthController(
    private val userService: UserService
) {
    @GetMapping("/csrf-token")
    fun getCsrfToken(csrfToken: CsrfToken): CsrfToken {
        return csrfToken
    }

    @GetMapping("/user")
    fun getUser(): GetUserResponse {
        val loginUser = SecurityContextHolder.getContext().authentication.principal as LoginUser
        val user = userService.getUser(loginUser.email)
        return GetUserResponse(user!!.id!!, user.email.value, user.name)
    }
}

data class GetUserResponse(
    val id: Int,
    val email: String,
    val name: String
)
