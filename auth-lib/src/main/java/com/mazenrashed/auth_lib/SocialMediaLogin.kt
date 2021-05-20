package com.mazenrashed.auth_lib

import android.app.Activity
import com.mazenrashed.auth_lib.login_by_social_media.*
import java.lang.Exception

class SocialMediaLogin {

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
        private var socialMediaLogin: SocialMediaLogin? = null

        @JvmStatic
        fun getInstance(): SocialMediaLogin {
            if (socialMediaLogin == null)
                socialMediaLogin =
                    SocialMediaLogin()

            return socialMediaLogin
                ?: getInstance()
        }

        fun init(googleClientId: String?, twitterClientId: String?, twitterSecretId: String?) {
            socialMediaLogin?.googleClientId = googleClientId
            socialMediaLogin?.twitterConsumerKey = twitterClientId
            socialMediaLogin?.twitterConsumerSecret = twitterSecretId

        }
    }

    enum class Platforms {
        GOOGLE,
        TWITTER,
        FACEBOOK
    }
}