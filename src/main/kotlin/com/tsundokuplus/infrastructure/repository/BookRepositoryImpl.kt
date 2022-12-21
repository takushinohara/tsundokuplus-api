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
        val results = BookTable.join(NoteTable, JoinType.LEFT, additionalConstraint = { BookTable.id eq NoteTable.book_id })
            .select { BookTable.user_id eq userId }
            .orderBy(NoteTable.updated_at to SortOrder.DESC)

        return results.map {
            Book(
                it[BookTable.id],
                it[BookTable.title],
                it[BookTable.author],
                it[BookTable.publisher],
                it[BookTable.thumbnail],
                it[BookTable.small_thumbnail],
                Note.ofNull()
            )
        }
    }

    override fun findOne(bookId: Int, userId: Int): Book? {
        val result = BookTable
            .join(NoteTable, JoinType.LEFT, additionalConstraint = { BookTable.id eq NoteTable.book_id })
            .select { BookTable.id eq bookId and (BookTable.user_id eq userId) }
            .single()

        return result.let {
            Book(
                it[BookTable.id],
                it[BookTable.title],
                it[BookTable.author],
                it[BookTable.publisher],
                it[BookTable.thumbnail],
                it[BookTable.small_thumbnail],
                Note(
                    it[NoteTable.contents],
                    it[NoteTable.updated_at]
                )
            )
        }
    }

    override fun create(book: Book, userId: Int) {
        val id = BookTable.insert {
            it[title] = book.title
            it[user_id] = userId
            it[author] = book.author
            it[publisher] = book.publisher
            it[thumbnail] = book.thumbnail
            it[small_thumbnail] = book.smallThumbnail
            it[created_at] = LocalDateTime.now(ZoneId.of("UTC"))
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        } get BookTable.id

        NoteTable.insert {
            it[book_id] = id
            it[created_at] = LocalDateTime.now(ZoneId.of("UTC"))
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        }
    }

    override fun update(book: Book) {
        val bookId: Int = book.id!!
        NoteTable.update ({ NoteTable.book_id eq bookId }) {
            it[contents] = book.note.contents
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        }
    }

    override fun delete(bookId: Int) {
        NoteTable.deleteWhere { book_id eq bookId }
        BookTable.deleteWhere { id eq bookId }
    }

    object BookTable : Table("book") {
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

    object NoteTable : Table("note") {
        val id = integer("id").autoIncrement()
        val book_id = integer("book_id")
        val contents = varchar("contents", 2048).nullable()
        val created_at = datetime("created_at")
        val updated_at = datetime("updated_at")
    }
}
