package com.tsundokuplus.application.config

import org.apache.catalina.Context
import org.apache.tomcat.util.http.Rfc6265CookieProcessor
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class TomcatContextCustomizerImpl (
    private val environment: Environment
) : TomcatContextCustomizer {
    override fun customize(context: Context) {
        val cookieProcessor = Rfc6265CookieProcessor()
        cookieProcessor.setSameSiteCookies(environment.getProperty("app.cookie.same-site"))
        context.cookieProcessor = cookieProcessor
    }
}
