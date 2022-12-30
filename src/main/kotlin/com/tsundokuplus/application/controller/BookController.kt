package com.tsundokuplus.application.controller

import com.tsundokuplus.application.service.BookService
import com.tsundokuplus.application.service.security.LoginUser
import com.tsundokuplus.domain.model.book.Book
import com.tsundokuplus.domain.model.book.Note
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("books")
@CrossOrigin
class BookController (
    private val bookService: BookService
) {
    @GetMapping
    fun getBooks(): GetBooksResponse {
        val books = bookService.getBooks(loginUser().id)
        return GetBooksResponse(books)
    }

    @GetMapping("/{book_id}")
    fun getBook(@PathVariable("book_id") bookId: Int): GetBookResponse {
        val book = bookService.getBook(bookId, loginUser().id)
        return GetBookResponse(
            book.id!!,
            book.title,
            book.author,
            book.publisher,
            book.thumbnail,
            book.note
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody request: AddBookRequest) {
        val book = Book(
            id = null,
            request.title,
            request.author,
            request.publisher,
            request.thumbnail,
            request.smallThumbnail,
            Note.ofNull()
        )
        bookService.addBook(book, loginUser().id)
    }

    @PutMapping("/{book_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateBook(@PathVariable("book_id") bookId: Int, @RequestBody request: UpdateBookRequest) {
        val note = Note(request.note)
        bookService.updateBook(bookId, note, loginUser().id)
    }

    @DeleteMapping("/{book_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable("book_id") bookId: Int) {
        bookService.deleteBook(bookId, loginUser().id)
    }

    private fun loginUser(): LoginUser {
        return SecurityContextHolder.getContext().authentication.principal as LoginUser
    }
}

data class GetBooksResponse(
    val books: List<Book>
)

data class GetBookResponse(
    val bookId: Int,
    val title: String,
    val author: String?,
    val publisher: String?,
    val thumbnail: String?,
    val note: Note
)

data class AddBookRequest(
    val title: String,
    val author: String?,
    val publisher: String?,
    val thumbnail: String?,
    val smallThumbnail: String?
)

data class UpdateBookRequest(
    val note: String?
)
