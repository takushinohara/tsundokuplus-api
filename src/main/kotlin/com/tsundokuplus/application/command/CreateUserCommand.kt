package com.tsundokuplus.application.command

import com.tsundokuplus.application.service.UserService
import com.tsundokuplus.domain.model.Email
import com.tsundokuplus.domain.model.Password
import com.tsundokuplus.domain.model.User
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class CreateUserCommand(
    private val userService: UserService,
) {
    @ShellMethod("Create a new user.")
    fun createUser() {
        print("email: ")
        val email = readLine()

        print("password: ")
        val password = System.console()?.readPassword() ?: readLine()

        print("name: ")
        val name = readLine()

        val user = User(
            email = Email(email.toString()),
            password = Password.factory(password.toString()),
            name = name.toString()
        )
        userService.createUser(user)

        println("Done!")
    }
}
