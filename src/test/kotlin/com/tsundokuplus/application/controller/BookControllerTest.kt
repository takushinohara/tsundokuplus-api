package com.tsundokuplus.application.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tsundokuplus.application.service.BookService
import com.tsundokuplus.domain.model.Book
import com.tsundokuplus.domain.model.Note
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.nio.charset.StandardCharsets

class BookControllerTest {
    private val bookService = mock<BookService>()
    private val bookController = BookController(bookService)
    private lateinit var mockMvc: MockMvc
    private lateinit var book: Book

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build()
        book = Book(
            1,
            "Test book",
            "Test author",
            "Test publisher",
            "http://example.com",
            "http://example.com",
            Note("Test note")
        )
    }

    @Test
    fun `Get a list of books`() {
        val books = listOf(book)
        whenever(bookService.getList()).thenReturn(books)

        val result = mockMvc.perform(get("/book/list"))
            .andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        Assertions.assertThat(result).isEqualTo(expected(GetBookListResponse(books)))
    }

    @Test
    fun `Get a book`() {
        val bookId = book.id!!
        whenever(bookService.getDetail(bookId)).thenReturn(book)

        val result = mockMvc.perform(get("/book/$bookId"))
            .andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        val response = GetBookResponse(bookId, book.title, book.author, book.publisher, book.thumbnail, book.note)
        Assertions.assertThat(result).isEqualTo(expected(response))
    }

    @Test
    fun `Add a book`() {
        val initialBook = Book(
            id = null,
            book.title,
            book.author,
            book.publisher,
            book.thumbnail,
            book.smallThumbnail,
            Note.ofNull()
        )
        doNothing().`when`(bookService).addBook(initialBook)

        val request = AddBookRequest(book.title, book.author, book.publisher, book.thumbnail, book.smallThumbnail)
        mockMvc.perform(post("/book")
            .content(ObjectMapper().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated)

        verify(bookService).addBook(initialBook)
    }

    @Test
    fun `Update a book`() {
        val bookId = book.id!!
        val updatedContents = "Test for update."
        doNothing().`when`(bookService).updateBook(bookId, Note(updatedContents))

        val request = UpdateBookRequest(updatedContents)
        mockMvc.perform(put("/book/$bookId")
            .content(ObjectMapper().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent)

        verify(bookService).updateBook(bookId, Note(updatedContents))
    }

    @Test
    fun `Delete a book`() {
        val bookId = book.id!!
        doNothing().`when`(bookService).deleteBook(bookId)

        mockMvc.perform(delete("/book/$bookId"))
            .andExpect(status().isNoContent)

        verify(bookService).deleteBook(bookId)
    }

    private fun expected(response: Any): String? {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .writeValueAsString(response)
    }
}
