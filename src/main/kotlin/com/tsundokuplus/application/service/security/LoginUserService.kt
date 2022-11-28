package com.tsundokuplus.application.service.security

import com.tsundokuplus.application.service.UserService
import com.tsundokuplus.domain.model.user.RoleType
import com.tsundokuplus.domain.model.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginUserService(
    private val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        val user = userService.getUser(username)
        return user?.let { LoginUser(user) }
    }
}

data class LoginUser(
    val id: Int,
    val email: String,
    val pass: String,
    val roleType: RoleType
) : UserDetails {

    constructor(user: User) : this(user.id!!, user.email.value, user.password.value, user.roleType)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return AuthorityUtils.createAuthorityList(this.roleType.toString())
    }

    override fun getPassword(): String {
        return this.pass
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
