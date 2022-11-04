package com.tsundokuplus.domain.model

import java.time.LocalDateTime

data class Note(
    val contents: String?,
    val UpdatedAt: LocalDateTime?
) {
    companion object {
        fun ofNull(): Note {
            return Note(null, null)
        }
    }
}
