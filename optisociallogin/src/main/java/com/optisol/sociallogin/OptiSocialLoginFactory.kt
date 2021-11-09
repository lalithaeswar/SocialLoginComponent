package com.optisol.sociallogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.optisol.sociallogin.activities.InstaSigninActivity
import com.optisol.sociallogin.activities.LinkedInSigninActivity
import com.optisol.sociallogin.core.FacebookSignInHelper
import com.optisol.sociallogin.core.GoogleSignInHelper
import com.optisol.sociallogin.core.TwitterSigninHelper
import com.optisol.sociallogin.listeners.LoginResultListener
import com.optisol.sociallogin.listeners.SocialLoginListener
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.helper.isNetworkAvailable

object OptiSocialLoginFactory {
    var loginHelper: SocialLoginListener?=null
        var listener:LoginResultListener?=null
        const val GOOGLE_LOGIN=100
        fun signIn(activity: Activity, type: LoginType, listener: LoginResultListener){
            this.listener=listener
            if(!isNetworkAvailable(activity)) {
                listener.onFailureLogin("Please check your internet Connection")
            }else {
                loginHelper = when (type) {
                    LoginType.GOOGLE -> GoogleSignInHelper(activity)
                    LoginType.FB -> FacebookSignInHelper(activity)
                    LoginType.TWITTER -> TwitterSigninHelper(activity)
                    LoginType.INSTAGRAM -> {
                        val loginActivity = Intent(activity, InstaSigninActivity::class.java)
                        activity.startActivity(loginActivity)
                        return
                    }
                    LoginType.LINKEDIN -> {
                        val loginActivity = Intent(activity, LinkedInSigninActivity::class.java)
                        activity.startActivity(loginActivity)
                        return
                    }

                }
                loginHelper?.setResultListener(listener)
                loginHelper?.signIn()
            }

        }
         fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
        loginHelper?.let {
            loginHelper?.onActivityResult(requestCode,resultCode,data)
        }
         }
        fun logout(activity: Activity, loginType: LoginType) {
            try {
                when (loginType) {
                    LoginType.GOOGLE-> GoogleSignInHelper(activity).signout()
                    LoginType.FB-> FacebookSignInHelper(activity).signout()
                    else -> Log.w("SocialLogin", "Unsupported logout for $loginType")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }






