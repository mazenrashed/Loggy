package com.mazenrashed.auth_lib.login_by_social_media

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginByGoogle(private val activity: Activity, clientId: String) :
    LoginBy {

    private var loginListener: ((SocialLoginResult) -> Unit)? = null
    private var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(clientId)
        .requestServerAuthCode(clientId)
        .requestEmail()
        .build()
    private val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    override fun login(loginListener: (SocialLoginResult) -> Unit) {
        this.loginListener = loginListener
        //allow user to choose login account again
        mGoogleSignInClient.signOut()

        activity.startActivityForResult(
            mGoogleSignInClient.signInIntent,
            RC_SIGN_IN
        )
    }

    private fun invokeLoginSuccess(
        accountId: String?,
        displayName: String?,
        email: String?,
        idToken: String?,
        serverAuthCode: String?
    ) {
        if (displayName != null && email != null && accountId != null && idToken != null && serverAuthCode != null)
            loginListener?.invoke(
                SocialLoginResult.Success(
                    token = idToken,
                    id = accountId,
                    name = displayName,
                    email = email,
                    serverAuthCode = serverAuthCode
                )
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            try {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            invokeLoginSuccess(
                accountId = account?.id,
                email = account?.email,
                displayName = account?.displayName,
                serverAuthCode = account?.serverAuthCode,
                idToken = account?.idToken
            )
        } catch (e: ApiException) {
            loginListener?.invoke(
                SocialLoginResult.Error(e)
            )
        }


    }

    companion object {
        const val RC_SIGN_IN = 12
    }
}
