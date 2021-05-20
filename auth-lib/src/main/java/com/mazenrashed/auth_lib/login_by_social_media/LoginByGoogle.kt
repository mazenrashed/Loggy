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
        .requestEmail()
        .build()
    private val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    override fun login(loginListener: (SocialLoginResult) -> Unit) {
        this.loginListener = loginListener

        val account = GoogleSignIn.getLastSignedInAccount(activity)

        if (account != null) {
            //user already logged in
            invokeLoginSuccess(account.id, account.email, account.displayName, account.idToken)
        } else {
            val signInIntent = mGoogleSignInClient.signInIntent
            activity.startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }
    }

    private fun invokeLoginSuccess(
        accountId: String?,
        displayName: String?,
        email: String?,
        idToken: String?
    ) {
        if (displayName != null && email != null && accountId != null && idToken != null)
            loginListener?.invoke(
                SocialLoginResult.Success(
                    idToken,
                    accountId,
                    displayName,
                    email
                )
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            invokeLoginSuccess(account?.id, account?.email, account?.displayName, account?.idToken)
        } catch (e: ApiException) {
            loginListener?.invoke(
                SocialLoginResult.Error(
                    e
                )
            )
        }
    }

    companion object {
        const val RC_SIGN_IN = 12
    }
}