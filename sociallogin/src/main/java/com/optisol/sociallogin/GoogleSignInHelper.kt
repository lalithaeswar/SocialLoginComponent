package com.optisol.sociallogin

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import java.util.*

 class GoogleSignInHelper(var activity: Activity):SocialLoginListener {
   var googleSignInClient: GoogleSignInClient
   var listener:LoginResultListener?=null

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
             activity.startActivityForResult(signInIntent, SocialLogin.GOOGLE_LOGIN)
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
                 listener?.onFailureLogin(e.message?:"")
             }
         }
     }

     override fun signout() {
         googleSignInClient?.signOut()
     }




    private fun handleSignInResult(account: GoogleSignInAccount) {
        if (account.getPhotoUrl() != null) {
            val result = LoginResult(
                true,
                LoginType.GOOGLE,
                id = account.id,
                email = account.email,
                firstName = account.givenName,
                lastName = account.familyName,
                token = account.idToken,
                avatar = if (account.getPhotoUrl() != null) account.photoUrl.toString() else ""

            )
            listener?.onSuccessLogin(result)

        }
    }

}