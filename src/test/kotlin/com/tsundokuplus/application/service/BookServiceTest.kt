package com.tsundokuplus.application.service

import com.tsundokuplus.domain.model.book.Book
import com.tsundokuplus.domain.model.book.Note
import com.tsundokuplus.domain.model.user.Email
import com.tsundokuplus.domain.model.user.Password
import com.tsundokuplus.domain.model.user.RoleType
import com.tsundokuplus.domain.model.user.User
import com.tsundokuplus.domain.repository.BookRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class BookServiceTest {
    private val bookRepository = mock<BookRepository>()
    private val bookService = BookService(bookRepository)
    private lateinit var book: Book
    private lateinit var user: User

    @BeforeEach
    fun setup() {
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
    fun `Get a list of books`() {
        val expected = listOf(book)

        whenever(bookRepository.findAll(user.id!!)).thenReturn(expected)

        val result = bookService.getList(user.id!!)
        Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Get a book`() {
        val expected = book

        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId, user.id!!)).thenReturn(book)

        val result = bookService.getDetail(bookId, user.id!!)
        Assertions.assertThat(result).isEqualTo(expected)

        verify(bookRepository).findOne(bookId, user.id!!)
    }

    @Test
    fun `Get a book - Throw exception when the book can't find`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId, user.id!!)).thenReturn(null)

        Assertions.assertThatIllegalArgumentException().isThrownBy {
            bookService.getDetail(bookId, user.id!!)
        }.withMessage("This book is not found")
    }

    @Test
    fun `Add a book`() {
        doNothing().`when`(bookRepository).create(book, user.id!!)

        bookService.addBook(book, user.id!!)

        verify(bookRepository).create(book, user.id!!)
    }

    @Test
    fun `Update a book`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId, user.id!!)).thenReturn(book)
        doNothing().`when`(bookRepository).update(book)

        val updatedContents = "Test for update."
        bookService.updateBook(bookId, Note(updatedContents), user.id!!)

        verify(bookRepository).findOne(bookId, user.id!!)
        verify(bookRepository).update(
            Book(
            bookId,
            "Test book",
            "Test author",
            "Test publisher",
            "http://example.com",
            "http://example.com",
            Note(updatedContents)
        )
        )
    }

    @Test
    fun `Update a book - Throw exception when the book can't find`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId, user.id!!)).thenReturn(null)

        Assertions.assertThatIllegalArgumentException().isThrownBy {
            bookService.updateBook(bookId, book.note, user.id!!)
        }.withMessage("This book is not found")

        verify(bookRepository, times(0)).update(any())
    }

    @Test
    fun `Delete a book`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId, user.id!!)).thenReturn(book)
        doNothing().`when`(bookRepository).delete(bookId)

        bookService.deleteBook(bookId, user.id!!)

        verify(bookRepository).findOne(bookId, user.id!!)
        verify(bookRepository).delete(bookId)
    }
}
