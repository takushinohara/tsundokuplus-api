package com.tsundokuplus.application.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class JsonRequestAuthenticationFilter(
    private val objectMapper: ObjectMapper,
    authenticationConfiguration: AuthenticationConfiguration
) : UsernamePasswordAuthenticationFilter(authenticationConfiguration.authenticationManager) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val jsonRequest = objectMapper.readValue(request.inputStream, EmailAndPasswordJsonRequest::class.java)
        val authRequest = UsernamePasswordAuthenticationToken(jsonRequest.email, jsonRequest.password)
        setDetails(request, authRequest)
        return super.getAuthenticationManager().authenticate(authRequest)
    }
}

data class EmailAndPasswordJsonRequest(
    val email: String,
    val password: String
)
