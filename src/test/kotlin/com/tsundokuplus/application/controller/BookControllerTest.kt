package com.tsundokuplus.application.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tsundokuplus.application.service.BookService
import com.tsundokuplus.application.service.security.LoginUser
import com.tsundokuplus.domain.model.book.Book
import com.tsundokuplus.domain.model.book.Note
import com.tsundokuplus.domain.model.user.Email
import com.tsundokuplus.domain.model.user.Password
import com.tsundokuplus.domain.model.user.RoleType
import com.tsundokuplus.domain.model.user.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.nio.charset.StandardCharsets

@ExtendWith(SpringExtension::class)
@ContextConfiguration
class BookControllerTest {
    private val bookService = mock<BookService>()
    private val bookController = BookController(bookService)
    private lateinit var mockMvc: MockMvc
    private lateinit var book: Book
    private lateinit var user: User

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
        user = User(
            1,
            Email("test@example.com"),
            Password.factory("password"),
            "Test User",
            RoleType.USER
        )
    }

    @Test
    @WithCustomMockUser
    fun `Get a list of books`() {
        val books = listOf(book)
        whenever(bookService.getList(user.id!!)).thenReturn(books)

        val result = mockMvc.perform(get("/book/list"))
            .andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        Assertions.assertThat(result).isEqualTo(expected(GetBookListResponse(books)))
    }

    @Test
    @WithCustomMockUser
    fun `Get a book`() {
        val bookId = book.id!!
        whenever(bookService.getDetail(bookId, user.id!!)).thenReturn(book)

        val result = mockMvc.perform(get("/book/$bookId"))
            .andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        val response = GetBookResponse(bookId, book.title, book.author, book.publisher, book.thumbnail, book.note)
        Assertions.assertThat(result).isEqualTo(expected(response))
    }

    @Test
    @WithCustomMockUser
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
        doNothing().`when`(bookService).addBook(initialBook, user.id!!)

        val request = AddBookRequest(book.title, book.author, book.publisher, book.thumbnail, book.smallThumbnail)
        mockMvc.perform(post("/book")
            .content(ObjectMapper().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated)

        verify(bookService).addBook(initialBook, user.id!!)
    }

    @Test
    @WithCustomMockUser
    fun `Update a book`() {
        val bookId = book.id!!
        val updatedContents = "Test for update."
        doNothing().`when`(bookService).updateBook(bookId, Note(updatedContents), user.id!!)

        val request = UpdateBookRequest(updatedContents)
        mockMvc.perform(put("/book/$bookId")
            .content(ObjectMapper().writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent)

        verify(bookService).updateBook(bookId, Note(updatedContents), user.id!!)
    }

    @Test
    @WithCustomMockUser
    fun `Delete a book`() {
        val bookId = book.id!!
        doNothing().`when`(bookService).deleteBook(bookId, user.id!!)

        mockMvc.perform(delete("/book/$bookId"))
            .andExpect(status().isNoContent)

        verify(bookService).deleteBook(bookId, user.id!!)
    }

    private fun expected(response: Any): String? {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .writeValueAsString(response)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithCustomMockUser(
    val id: Int = 1,
    val email: String = "test@example.com",
    val password: String = "password",
    val roleType: RoleType = RoleType.USER
)

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithCustomMockUser> {
    override fun createSecurityContext(customUser: WithCustomMockUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val principal = LoginUser(customUser.id, customUser.email, customUser.password, customUser.roleType)
        val auth = UsernamePasswordAuthenticationToken(principal, principal.password, principal.authorities)
        context.authentication = auth
        return context
    }
}
