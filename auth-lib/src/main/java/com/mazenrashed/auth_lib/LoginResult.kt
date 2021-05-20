package com.mazenrashed.auth_lib

sealed class LoginResult {
    data class Success(
        var token: String
    ) : LoginResult()

    data class Error(var throwable: Throwable) : LoginResult()
}
