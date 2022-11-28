package com.tsundokuplus.domain.model.user

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotEmpty())
    }
}
