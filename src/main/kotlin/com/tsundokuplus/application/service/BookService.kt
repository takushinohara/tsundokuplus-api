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
    fun getList(userId: Int): List<Book> {
        return bookRepository.findAll(userId)
    }

    @Transactional
    fun getDetail(bookId: Int, userId: Int): Book {
        return bookRepository.findOne(bookId, userId) ?: throw IllegalArgumentException("This book is not found")
    }

    @Transactional
    fun addBook(book: Book, userId: Int) {
        bookRepository.create(book, userId)
    }

    @Transactional
    fun updateBook(bookId: Int, note: Note, userId: Int) {
        val book = bookRepository.findOne(bookId, userId) ?: throw IllegalArgumentException("This book is not found")
        book.note = note
        bookRepository.update(book)
    }

    @Transactional
    fun deleteBook(bookId: Int, userId: Int) {
        bookRepository.findOne(bookId, userId) ?: throw IllegalArgumentException("This book is not found")
        bookRepository.delete(bookId)
    }
}
