package com.tsundokuplus.domain.repository

import com.tsundokuplus.domain.model.Book

interface BookRepository {
    fun findAll(): List<Book>
    fun findOne(bookId: Int): Book?
    fun create(book: Book)
    fun update(book: Book)
    fun delete(bookId: Int)
}
