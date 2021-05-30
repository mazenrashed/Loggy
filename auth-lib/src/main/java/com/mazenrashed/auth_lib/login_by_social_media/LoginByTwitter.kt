package com.mazenrashed.auth_lib.login_by_social_media

import android.app.Activity
import android.content.Intent
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient

class LoginByTwitter(private val activity: Activity, consumerKey: String, consumerSecret: String) :
    LoginBy {

    init {
        Twitter.initialize(
            TwitterConfig.Builder(activity.applicationContext)
                .twitterAuthConfig(
                    TwitterAuthConfig(
                        consumerKey,
                        consumerSecret
                    )
                )
                .debug(true)
                .build()
        )
    }

    private val twitterAuth = TwitterAuthClient()

    override fun login(loginListener: (SocialLoginResult) -> Unit) {
        twitterAuth.authorize(activity, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                result?.data?.also {
                    loginListener(
                        SocialLoginResult.Success(
                            token = it.authToken.token,
                            id = it.userId.toString(),
                            name = it.userName,
                            email = "",
                            tokenSecret = it.authToken.secret
                        )
                    )
                }

            }

            override fun failure(exception: TwitterException?) {
                exception?.printStackTrace()
                exception?.also {
                    loginListener(
                        SocialLoginResult.Error(
                            it
                        )
                    )
                }
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        twitterAuth.onActivityResult(requestCode, resultCode, data)
    }
}