package com.mazenrashed.auth_lib

import android.app.Activity
import android.content.Intent
import com.mazenrashed.auth_lib.login_by_social_media.*
import java.lang.Exception

class Loggy private constructor(){

    private var googleClientId: String? = null
    private var twitterConsumerKey: String? = null
    private var twitterConsumerSecret: String? = null
    private var loginBy : LoginBy? = null

    fun loginBySocialMedia(
        platform: Platforms,
        activity: Activity,
        resultCallback: ((LoginResult) -> Unit)
    ) {
        loginBy = when (platform) {
            Platforms.GOOGLE -> LoginByGoogle(
                activity,
                googleClientId
                    ?: throw Exception(NOT_INIT_MESSAGE)
            )
            Platforms.TWITTER -> LoginByTwitter(
                activity,
                twitterConsumerKey
                    ?: throw Exception(NOT_INIT_MESSAGE),
                twitterConsumerSecret
                    ?: throw Exception(NOT_INIT_MESSAGE)
            )
            Platforms.FACEBOOK -> LoginByFacebook(
                activity
            )
        }

        loginBy?.login {
            when (it) {
                is SocialLoginResult.Error -> {
                    resultCallback(LoginResult.Error(it.throwable))
                }

                is SocialLoginResult.Success -> {
                    resultCallback(LoginResult.Success(it.token))
                }
            }
        }
    }

    fun init(googleClientId: String?, twitterClientId: String?, twitterSecretId: String?) {
        instance.googleClientId = googleClientId
        instance.twitterConsumerKey = twitterClientId
        instance.twitterConsumerSecret = twitterSecretId

    }

    fun setOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        loginBy?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val NOT_INIT_MESSAGE =
            "use loggy.init() to setup the lib"
        var instance: Loggy = Loggy()
    }

    enum class Platforms {
        GOOGLE,
        TWITTER,
        FACEBOOK
    }
}