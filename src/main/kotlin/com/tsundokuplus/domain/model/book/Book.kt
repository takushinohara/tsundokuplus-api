package com.tsundokuplus.domain.model.book

data class Book(
    val id: Int? = null,
    val title: String,
    val author: String?,
    val publisher: String?,
    val thumbnail: String?,
    val smallThumbnail: String?,
    var note: Note = Note.ofNull()
)
