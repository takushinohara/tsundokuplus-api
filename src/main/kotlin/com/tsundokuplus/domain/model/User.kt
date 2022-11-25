package com.tsundokuplus.domain.model

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val roleType: RoleType
)
