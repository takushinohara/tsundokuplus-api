package com.tsundokuplus.application.controller

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class AuthController {
    @GetMapping("/csrf-token")
    fun getCsrfToken(csrfToken: CsrfToken): CsrfToken {
        return csrfToken
    }
}
