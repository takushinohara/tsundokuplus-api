package com.tsundokuplus.domain.model

data class User(
    val id: Int? = null,
    val email: Email,
    val password: Password,
    val name: String,
    val roleType: RoleType = RoleType.USER
)
