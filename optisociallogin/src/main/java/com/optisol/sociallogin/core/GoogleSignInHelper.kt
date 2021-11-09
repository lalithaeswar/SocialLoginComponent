package com.optisol.sociallogin.core

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.optisol.sociallogin.OptiSocialLoginFactory
import com.optisol.sociallogin.listeners.LoginResultListener
import com.optisol.sociallogin.listeners.SocialLoginListener
import com.optisol.sociallogin.model.LoginResult
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.helper.checkNetworkAvailable
import com.optisol.sociallogin.helper.isNetworkAvailable

class GoogleSignInHelper(var activity: Activity): SocialLoginListener {
   var googleSignInClient: GoogleSignInClient
   var listener: LoginResultListener?=null

     init {
         val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestEmail()
             .build()
         googleSignInClient = GoogleSignIn.getClient(activity, gso)
     }


     override fun setResultListener(listener: LoginResultListener) {
         this.listener=listener
     }


     override fun signIn() {//check whether user already logged or not
         val account = GoogleSignIn.getLastSignedInAccount(activity)
         if(account!=null) { //if already logged
             handleSignInResult(account)

         }else {
             val signInIntent = googleSignInClient.signInIntent
             activity.startActivityForResult(signInIntent, OptiSocialLoginFactory.GOOGLE_LOGIN)
         }
     }

     override fun onActivityResult(
         requestCode: Int,
         resultCode: Int,
         data: Intent?
     ) {
         if (data!=null) {
             val task = GoogleSignIn.getSignedInAccountFromIntent(data)
             try {
                 val account = task.getResult(ApiException::class.java)
                 handleSignInResult(account)

             } catch (e: ApiException) {
                 listener?.onFailureLogin(e.localizedMessage?:"")

             }
         }
     }

     override fun signout() {
         googleSignInClient.signOut()
     }

    private fun handleSignInResult(account: GoogleSignInAccount) {
            val result = LoginResult(
                true,
                LoginType.GOOGLE,
                id = account.id,
                email = account.email,
                firstName = account.givenName,
                lastName = account.familyName,
                token = account.idToken,
                avatar = if (account.photoUrl != null) account.photoUrl?.toString() else ""

            )
            listener?.onSuccessLogin(result)
    }

}