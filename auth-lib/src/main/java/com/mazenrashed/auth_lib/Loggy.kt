package com.mazenrashed.auth_lib

import android.app.Activity
import com.mazenrashed.auth_lib.login_by_social_media.*
import java.lang.Exception

class Loggy {

    private var googleClientId: String? = null
    private var twitterConsumerKey: String? = null
    private var twitterConsumerSecret: String? = null

    fun loginBySocialMedia(
        platform: Platforms,
        activity: Activity,
        resultCallback: ((LoginResult) -> Unit)
    ) {
        val login: LoginBy = when (platform) {
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

        login.login {
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



    companion object {
        private const val NOT_INIT_MESSAGE =
            "use SocialMediaLogin.getInstance().init() to setup the lib"
        private var loggy: Loggy? = null

        @JvmStatic
        fun getInstance(): Loggy {
            if (loggy == null)
                loggy =
                    Loggy()

            return loggy
                ?: getInstance()
        }

        fun init(googleClientId: String?, twitterClientId: String?, twitterSecretId: String?) {
            loggy?.googleClientId = googleClientId
            loggy?.twitterConsumerKey = twitterClientId
            loggy?.twitterConsumerSecret = twitterSecretId

        }
    }

    enum class Platforms {
        GOOGLE,
        TWITTER,
        FACEBOOK
    }
}