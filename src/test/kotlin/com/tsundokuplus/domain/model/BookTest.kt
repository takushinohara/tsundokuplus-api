package com.tsundokuplus.domain.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BookTest {
    @Test
    fun `Books can have a blank note`() {
        val book = Book(
            1,
            "Test book",
            "Test author",
            "Test publisher",
            "http://example.com",
            "http://example.com",
            Note.ofNull()
        )

        Assertions.assertThat(book.note.contents).isNull()
        Assertions.assertThat(book.note.updatedAt).isNull()
    }
}
