package com.tsundokuplus.application.controller

import com.tsundokuplus.application.service.BookService
import com.tsundokuplus.domain.model.Book
import com.tsundokuplus.domain.model.Note
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("book")
@CrossOrigin
class BookController (
    private val bookService: BookService
) {
    @GetMapping("/list")
    fun getBookList(): GetBookListResponse {
        val books = bookService.getList()
        return GetBookListResponse(books)
    }

    @GetMapping("/{book_id}")
    fun getBook(@PathVariable("book_id") bookId: Int): GetBookResponse {
        val book = bookService.getDetail(bookId)
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
        bookService.addBook(book)
    }

    @PutMapping("/{book_id}")
    fun updateBook(@PathVariable("book_id") bookId: Int, @RequestBody request: UpdateBookRequest) {
        val note = Note(request.note, LocalDateTime.now())
        bookService.updateBook(bookId, note)
    }

    @DeleteMapping("/{book_id}")
    fun deleteBook(@PathVariable("book_id") bookId: Int) {
        bookService.deleteBook(bookId)
    }
}

data class GetBookListResponse(
    val bookList: List<Book>
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
