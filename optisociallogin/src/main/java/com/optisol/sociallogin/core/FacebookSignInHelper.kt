package com.optisol.sociallogin.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.WorkerThread
import com.facebook.*
import com.facebook.login.LoginManager
import com.optisol.sociallogin.listeners.LoginResultListener
import com.optisol.sociallogin.listeners.SocialLoginListener
import com.optisol.sociallogin.model.LoginResult
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.model.Constant
import org.json.JSONObject


class FacebookSignInHelper(var activity: Activity): SocialLoginListener {

    var listener: LoginResultListener?=null
    private val  callbackManager = CallbackManager.Factory.create()

    override fun setResultListener(listener: LoginResultListener) {
        this.listener=listener
    }
    private val fbCallback = object : FacebookCallback<com.facebook.login.LoginResult> {
        override fun onSuccess(result: com.facebook.login.LoginResult) {
            handleSignInResult(result.accessToken)
        }

        override fun onCancel() {
            listener?.onFailureLogin(Constant.FACEBOOK_CANCELLED)
        }

        override fun onError(error: FacebookException) {
            listener?.onFailureLogin(error.localizedMessage?:"Unable to Login the facebook")
        }
    }


    override fun signIn() {//check whether user already logged or not
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
           handleSignInResult(accessToken)
        } else {
            LoginManager.getInstance().registerCallback(callbackManager, fbCallback)
            val permissions = mutableSetOf("public_profile", "email")
          //  permissions += extraPermissions
            LoginManager.getInstance()
                .logInWithReadPermissions(activity, permissions)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun signout() {
        LoginManager.getInstance().logOut()
    }



    @WorkerThread
    private fun handleSignInResult(accessToken: AccessToken) {

        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`: JSONObject?,  response: GraphResponse? ->
            val jsonObject = response!!.getJSONObject()
            try {
                val email = jsonObject!!.getString("email")
                val firstName = jsonObject.getString("first_name")
                val lastName = jsonObject.getString("last_name")
                val id=jsonObject.getString("id")
                val avatar = jsonObject.optJSONObject("picture")
                    ?.optJSONObject("data")
                    ?.getString("url")
                listener?.onSuccessLogin(
                    LoginResult(
                        true,
                        LoginType.FB,
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        avatar = avatar,
                            id = id
                    )
                )
            } catch (e: Exception) {
                Log.v("tag", "Exception: " + e.localizedMessage)
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, name, first_name,last_name, email, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }



}