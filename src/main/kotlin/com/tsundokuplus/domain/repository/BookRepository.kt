package com.tsundokuplus.domain.repository

import com.tsundokuplus.domain.model.book.Book

interface BookRepository {
    fun findAll(userId: Int): List<Book>
    fun findOne(bookId: Int, userId: Int): Book
    fun create(book: Book, userId: Int)
    fun update(book: Book)
    fun delete(bookId: Int)
}
