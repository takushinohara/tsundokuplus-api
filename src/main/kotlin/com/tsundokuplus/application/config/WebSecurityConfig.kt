package com.tsundokuplus.application.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.tsundokuplus.application.service.security.LoginUserService
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
class WebSecurityConfig(
    private val objectMapper: ObjectMapper,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val loginUserService: LoginUserService,
    private val environment: Environment
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .mvcMatchers("/login").permitAll()
            .anyRequest().authenticated()

        val authFilter = JsonRequestAuthenticationFilter(objectMapper, authenticationConfiguration)
        authFilter.setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/login", "POST"))
        authFilter.setSessionAuthenticationStrategy(ChangeSessionIdAuthenticationStrategy())
        authFilter.setAuthenticationSuccessHandler { _, response, _ -> response.status = HttpServletResponse.SC_OK }
        authFilter.setAuthenticationFailureHandler { _, response, _ -> response.status = HttpServletResponse.SC_UNAUTHORIZED }

        http.addFilter(authFilter)

        http.exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .accessDeniedHandler { _, response, _ -> response.status = HttpServletResponse.SC_UNAUTHORIZED }

        http.cors().configurationSource(corsConfigurationSource())

        http.csrf().ignoringAntMatchers("/login")

        http.logout()
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .logoutSuccessHandler { _, response, _ -> response.status = HttpServletResponse.SC_OK }

        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(loginUserService)
        authProvider.setPasswordEncoder(BCryptPasswordEncoder())

        return authProvider
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(environment.getProperty("app.cors.allowed-origin"))
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("Content-Type", "X-CSRF-TOKEN")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}
