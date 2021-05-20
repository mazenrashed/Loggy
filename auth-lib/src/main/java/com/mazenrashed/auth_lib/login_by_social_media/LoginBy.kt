package com.mazenrashed.auth_lib.login_by_social_media

import android.content.Intent

interface LoginBy {

    fun login(loginListener: (SocialLoginResult) -> Unit)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}