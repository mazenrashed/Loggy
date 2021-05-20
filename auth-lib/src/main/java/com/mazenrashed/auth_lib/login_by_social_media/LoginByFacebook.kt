package com.mazenrashed.auth_lib.login_by_social_media

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult


class LoginByFacebook(private val activity: Activity) :
    LoginBy {

    private val mCallbackManager = CallbackManager.Factory.create()

    override fun login(loginListener: (SocialLoginResult) -> Unit) {
        com.facebook.login.LoginManager.getInstance()
            .registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        val request = GraphRequest.newMeRequest(
                            loginResult?.accessToken
                        ) { jsonObject, response ->
                            val name = jsonObject.getString("name")
                            val id = jsonObject.getString("id")

                            loginListener(
                                SocialLoginResult.Success(
                                    loginResult?.accessToken?.token?:"",
                                    id,
                                    name,
                                    ""
                                )
                            )

                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,gender,birthday")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Toast.makeText(activity, "Login Cancel", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(exception: FacebookException) {
                        loginListener(
                            SocialLoginResult.Error(
                                exception
                            )
                        )
                    }
                })

        com.facebook.login.LoginManager.getInstance()
            .logInWithReadPermissions(activity, listOf("public_profile"))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

}