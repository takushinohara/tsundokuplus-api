package com.tsundokuplus.infrastructure.repository

import com.tsundokuplus.domain.model.user.Email
import com.tsundokuplus.domain.model.user.Password
import com.tsundokuplus.domain.model.user.RoleType
import com.tsundokuplus.domain.model.user.User
import com.tsundokuplus.domain.repository.UserRepository
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneId

@Repository
class UserRepositoryImpl : UserRepository {
    override fun findOne(email: String): User? {
        val result = UsersTable.select { UsersTable.email eq email }.single()

        return result.let {
            User(
                it[UsersTable.id],
                Email(it[UsersTable.email]) ,
                Password(it[UsersTable.password]),
                it[UsersTable.name],
                it[UsersTable.roleType]
            )
        }
    }

    override fun create(user: User) {
        UsersTable.insert {
            it[email] = user.email.value
            it[password] = user.password.value
            it[name] = user.name
            it[roleType] = user.roleType
            it[created_at] = LocalDateTime.now(ZoneId.of("UTC"))
            it[updated_at] = LocalDateTime.now(ZoneId.of("UTC"))
        }
    }

    object UsersTable : Table("users") {
        val id = integer("id").autoIncrement()
        val email = varchar("email", 255)
        val password = varchar("password", 128)
        val name = varchar("name", 255)
        val roleType = customEnumeration("role_type", "ENUM('ADMIN', 'USER')", { value -> RoleType.valueOf(value as String) }, { it.name })
        val created_at = datetime("created_at")
        val updated_at = datetime("updated_at")
    }
}
