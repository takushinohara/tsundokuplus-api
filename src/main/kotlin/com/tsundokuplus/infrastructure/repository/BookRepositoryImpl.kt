package com.tsundokuplus.infrastructure.repository

import com.tsundokuplus.domain.model.book.Book
import com.tsundokuplus.domain.model.book.Note
import com.tsundokuplus.domain.repository.BookRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.datetime
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneId

@Repository
class BookRepositoryImpl : BookRepository {
    override fun findAll(userId: Int): List<Book> {
        val results = BooksTable.join(NotesTable, JoinType.LEFT, additionalConstraint = { BooksTable.id eq NotesTable.book_id })
            .select { BooksTable.user_id eq userId }
            .orderBy(NotesTable.updated_at to SortOrder.DESC)

        return results.map {
            Book(
                it[BooksTable.id],
                it[BooksTable.title],
                it[BooksTable.author],
                it[BooksTable.publisher],
                it[BooksTable.thumbnail],
                it[BooksTable.small_thumbnail],
                Note.ofNull()
            )
        }
    }

    override fun findOne(bookId: Int, userId: Int): Book {
        val result = BooksTable
            .join(NotesTable, JoinType.LEFT, additionalConstraint = { BooksTable.id eq NotesTable.book_id })
            .select { BooksTable.id eq bookId and (BooksTable.user_id eq userId) }
            .single()

        return result.let {
            Book(
                it[BooksTable.id],
                it[BooksTable.title],
                it[BooksTable.author],
                it[BooksTable.publisher],
                it[BooksTable.thumbnail],
                it[BooksTable.small_thumbnail],
                Note(
                    it[NotesTable.contents],
                    it[NotesTable.updated_at]
                )
            )
        }
    }

    override fun create(book: Book, userId: Int) {
        val id = BooksTable.insert {
            it[title] = book.title
            it[user_id] = userId
            it[author] = book.author
            it[publisher] = book.publisher
            it[thumbnail] = book.thumbnail
            it[small_thumbnail] = book.smallThumbnail
            it[created_at] = LocalDateTime.now(ZoneId.of("UTC"))
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        } get BooksTable.id

        NotesTable.insert {
            it[book_id] = id
            it[created_at] = LocalDateTime.now(ZoneId.of("UTC"))
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        }
    }

    override fun update(book: Book) {
        val bookId: Int = book.id!!
        NotesTable.update ({ NotesTable.book_id eq bookId }) {
            it[contents] = book.note.contents
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        }
    }

    override fun delete(bookId: Int) {
        NotesTable.deleteWhere { book_id eq bookId }
        BooksTable.deleteWhere { id eq bookId }
    }

    object BooksTable : Table("books") {
        val id = integer("id").autoIncrement()
        val user_id = integer("user_id")
        val title = varchar("title", 255)
        val author = varchar("author", 255).nullable()
        val publisher = varchar("publisher", 255).nullable()
        val thumbnail = varchar("thumbnail", 255).nullable()
        val small_thumbnail = varchar("small_thumbnail", 255).nullable()
        val created_at = datetime("created_at")
        val updated_at = datetime("updated_at")
    }

    object NotesTable : Table("notes") {
        val id = integer("id").autoIncrement()
        val book_id = integer("book_id")
        val contents = varchar("contents", 2048).nullable()
        val created_at = datetime("created_at")
        val updated_at = datetime("updated_at")
    }
}
