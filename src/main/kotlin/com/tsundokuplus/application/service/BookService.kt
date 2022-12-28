package com.tsundokuplus.application.service

import com.tsundokuplus.application.exception.BookNotFoundException
import com.tsundokuplus.domain.model.book.Book
import com.tsundokuplus.domain.model.book.Note
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
        try {
            return bookRepository.findOne(bookId, userId)
        } catch (e: NoSuchElementException) {
            println(e.stackTraceToString())
            throw BookNotFoundException("This book is not found")
        }
    }

    @Transactional
    fun addBook(book: Book, userId: Int) {
        bookRepository.create(book, userId)
    }

    @Transactional
    fun updateBook(bookId: Int, note: Note, userId: Int) {
        try {
            val book = bookRepository.findOne(bookId, userId)
            book.note = note
            bookRepository.update(book)
        } catch (e: NoSuchElementException) {
            println(e.stackTraceToString())
            throw BookNotFoundException("This book is not found")
        }
    }

    @Transactional
    fun deleteBook(bookId: Int, userId: Int) {
        try {
            bookRepository.findOne(bookId, userId)
            bookRepository.delete(bookId)
        } catch (e: NoSuchElementException) {
            println(e.stackTraceToString())
            throw BookNotFoundException("This book is not found")
        }
    }
}
