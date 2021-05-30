package com.mazenrashed.auth_lib.login_by_social_media

sealed class SocialLoginResult {
    data class Success(
        var token: String,
        var id: String,
        var name: String,
        var email: String,
        var tokenSecret: String? = null, //For twitter
        var serverAuthCode: String? = null //For google
    ) : SocialLoginResult()

    data class Error(var throwable: Throwable) : SocialLoginResult()
}
