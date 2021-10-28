package com.optisol.sociallogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log

class SocialLogin {

    companion object{
        var loginHelper:SocialLoginListener?=null
        val GOOGLE_LOGIN=100
        val FACEBOOK_LOGIN=101
        val INSTAGRAM_LOGIN=102
        val TWITTER_LOGIN=103
        fun signIn(activity: Activity,type:LoginType,listener:LoginResultListener){
            loginHelper = when (type) {
                LoginType.GOOGLE -> GoogleSignInHelper(activity)
                LoginType.FB ->  FacebookSignInHelper(activity)
                LoginType.TWITTER-> GoogleSignInHelper(activity)
                LoginType.INSTAGRAM -> GoogleSignInHelper(activity)

            }
            loginHelper?.setResultListener(listener)
            loginHelper?.signIn()

        }
         fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
        loginHelper?.let {
            when(requestCode){
                GOOGLE_LOGIN-> loginHelper?.onActivityResult(requestCode,resultCode,data)
                else->{ loginHelper?.onActivityResult(requestCode,resultCode,data)}
            }
        }
         }
        fun logout(activity: Activity, loginType: LoginType) {
            try {
                when (loginType) {
                    LoginType.GOOGLE-> GoogleSignInHelper(activity).signout()
                    LoginType.FB-> FacebookSignInHelper(activity).signout()
                    LoginType.INSTAGRAM -> GoogleSignInHelper(activity).signout()
                    else -> Log.w("SocialLogin", "Unsupported logout for $loginType")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }





}