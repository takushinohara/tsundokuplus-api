package com.tsundokuplus.application.controller

import com.tsundokuplus.application.exception.BookNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(BookNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun bookNotFoundExceptionHandler(e: BookNotFoundException): ErrorResponse {
        logger.warn(e.stackTraceToString())
        return ErrorResponse(e.message)
    }
}

data class ErrorResponse(
    val message: String?
)
