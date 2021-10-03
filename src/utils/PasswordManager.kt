package com.mindeurfou.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordManager {

    fun validatePassword(attempt: String, userPassword: String): Boolean {
        return BCrypt.checkpw(attempt, userPassword)
    }

    fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

}