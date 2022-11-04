package com.tsundokuplus.application.service

import com.tsundokuplus.domain.model.Book
import com.tsundokuplus.domain.model.Note
import com.tsundokuplus.domain.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository
) {
    @Transactional
    fun getList(): List<Book> {
        return bookRepository.findAll()
    }

    @Transactional
    fun getDetail(bookId: Int): Book {
        return bookRepository.findOne(bookId) ?: throw IllegalArgumentException("This book is not found")
    }

    @Transactional
    fun addBook(book: Book) {
        bookRepository.create(book)
    }

    @Transactional
    fun updateBook(bookId: Int, note: Note) {
        val book = bookRepository.findOne(bookId) ?: throw IllegalArgumentException("This book is not found")
        book.note = note
        bookRepository.update(book)
    }

    @Transactional
    fun deleteBook(bookId: Int) {
        bookRepository.delete(bookId)
    }
}
