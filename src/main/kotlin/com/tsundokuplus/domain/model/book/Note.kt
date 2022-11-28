package com.tsundokuplus.domain.model.book

import java.time.LocalDateTime

data class Note(
    val contents: String?,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun ofNull(): Note {
            return Note(null, null)
        }
    }
}
