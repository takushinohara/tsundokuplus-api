package com.tsundokuplus.domain.model

data class Book(
    val id: Int?,
    val title: String,
    val author: String?,
    val publisher: String?,
    val thumbnail: String?,
    val smallThumbnail: String?,
    var note: Note
)
