package com.tsundokuplus.application.service

import com.tsundokuplus.domain.model.Book
import com.tsundokuplus.domain.model.Note
import com.tsundokuplus.domain.repository.BookRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class BookServiceTest {
    private val bookRepository = mock<BookRepository>()
    private val bookService = BookService(bookRepository)
    private lateinit var book: Book

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
    }

    @Test
    fun `Get a list of books`() {
        val expected = listOf(book)

        whenever(bookRepository.findAll()).thenReturn(expected)

        val result = bookService.getList()
        Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Get a book`() {
        val expected = book

        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId)).thenReturn(book)

        val result = bookService.getDetail(bookId)
        Assertions.assertThat(result).isEqualTo(expected)

        verify(bookRepository).findOne(bookId)
    }

    @Test
    fun `Get a book - Throw exception when the book can't find`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId)).thenReturn(null)

        Assertions.assertThatIllegalArgumentException().isThrownBy {
            bookService.getDetail(bookId)
        }.withMessage("This book is not found")
    }

    @Test
    fun `Add a book`() {
        doNothing().`when`(bookRepository).create(book)

        bookService.addBook(book)

        verify(bookRepository).create(book)
    }

    @Test
    fun `Update a book`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId)).thenReturn(book)
        doNothing().`when`(bookRepository).update(book)

        val updatedContents = "Test for update."
        bookService.updateBook(bookId, Note(updatedContents))

        verify(bookRepository).findOne(bookId)
        verify(bookRepository).update(Book(
            bookId,
            "Test book",
            "Test author",
            "Test publisher",
            "http://example.com",
            "http://example.com",
            Note(updatedContents)
        ))
    }

    @Test
    fun `Update a book - Throw exception when the book can't find`() {
        val bookId = book.id!!
        whenever(bookRepository.findOne(bookId)).thenReturn(null)

        Assertions.assertThatIllegalArgumentException().isThrownBy {
            bookService.updateBook(bookId, book.note)
        }.withMessage("This book is not found")

        verify(bookRepository, times(0)).update(any())
    }

    @Test
    fun `Delete a book`() {
        val bookId = book.id!!
        doNothing().`when`(bookRepository).delete(bookId)

        bookService.deleteBook(bookId)

        verify(bookRepository).delete(bookId)
    }
}
