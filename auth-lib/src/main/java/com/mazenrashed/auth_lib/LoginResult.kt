package com.mazenrashed.auth_lib

sealed class LoginResult {
    data class Success(
        var token: String,
        var tokenSecret: String? = null
    ) : LoginResult()

    data class Error(var throwable: Throwable) : LoginResult()
}
