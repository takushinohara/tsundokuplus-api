package com.tsundokuplus.application.config

import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Configuration
import javax.servlet.ServletContext

@Configuration
class ServletContextInitializerImpl : ServletContextInitializer {
    override fun onStartup(servletContext: ServletContext) {
        servletContext.sessionCookieConfig.isSecure = true
    }
}
