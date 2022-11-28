package com.tsundokuplus.domain.model

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotEmpty())
    }
}
