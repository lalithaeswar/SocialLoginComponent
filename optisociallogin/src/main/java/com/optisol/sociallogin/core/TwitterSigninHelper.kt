package com.optisol.sociallogin.core

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.listeners.LoginResultListener
import com.optisol.sociallogin.listeners.SocialLoginListener
import com.optisol.sociallogin.model.LoginResult
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User


class TwitterSigninHelper(var activity: Activity): SocialLoginListener {
    //Twitter Keys
    //Using optisol credential
    val TWITTER_KEY ="QAYy54u0T5TgcnH3Gd4CCl52z"// "6qjzMWursFCs8g9FvzuFfWMMn"
    val TWITTER_SECRET = "hCda3ry9kWGu9dpKZc9O1h1VtptuMLDifIrStSjT6ja8nsQJHt"//""zT4BtbVPDuj2FYpCZvBSGYC2NFhcPvWD8funrw25Z998udoXG3"
    init {
        val authConfig =TwitterAuthConfig(
            TWITTER_KEY,
            TWITTER_SECRET
        )

        val builder: TwitterConfig.Builder =
       TwitterConfig.Builder(activity)
        builder.twitterAuthConfig(authConfig)
        Twitter.initialize(builder.build())
    }



    var listener: LoginResultListener?=null

    val   mTwitterAuthClient = TwitterAuthClient()

    override fun setResultListener(listener: LoginResultListener) {
        this.listener=listener
    }


    override fun signIn() {//check whether user already logged or not

//        mTwitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
//            override fun success(result: Result<TwitterSession>) {
//                try {
//                    requestEmail(session = result.data)
//                } catch (e: Exception) {
//                    Log.v("tag", "Exception: " + e.localizedMessage)
//                }
//            }
//
//            override fun failure(exception: TwitterException) {
//                Log.v("tag", "Exception: " + exception.localizedMessage)
//                val viewIntent = Intent(
//                    "android.intent.action.VIEW",
//                    Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android&hl=en")
//                )
//                activity.startActivity(viewIntent)
//            }
//        })


        val twitterSession = TwitterCore.getInstance().sessionManager.activeSession

        if (twitterSession == null) {
          mTwitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>) {
                    val twitterSessio = result.data
                    getTwitterData(twitterSessio)
                }

                override fun failure(e: TwitterException) {
                    Log.e("Twitter", "Failed to authenticate user " + e.message)
                }
            })
        } else {
            getTwitterData(twitterSession)
        }

    }
    private fun requestEmail(session: TwitterSession){
        mTwitterAuthClient.requestEmail(session, object : Callback<String?>() {
            override fun success(result: Result<String?>?) {
                // Do something with the result, which provides the email address
                handleSignInResult(session, result?.data ?: "")
            }

            override fun failure(exception: TwitterException?) {
                // Do something on failure
                handleSignInResult(session, "")
            }
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        //Twitter
        mTwitterAuthClient?.let{mTwitterAuthClient.onActivityResult(requestCode, resultCode, data)}
    }

    override fun signout() {

    }




    private fun handleSignInResult(session: TwitterSession, email: String?) {

            val result = LoginResult(
                true,
                LoginType.GOOGLE,
                id = session.userId.toString(),
                email = email,
                firstName = session.userName,
                lastName = "",
                token = session.authToken.token,
                avatar = ""

            )
            listener?.onSuccessLogin(result)


    }
    private fun getTwitterData(twitterSession: TwitterSession) {
        val twitterApiClient = TwitterApiClient(twitterSession)
        val getUserCall =
            twitterApiClient.accountService.verifyCredentials(true, false, true)
        getUserCall.enqueue(object : Callback<User>() {
            override fun success(result: Result<User>) {
                var socialId = ""
                var firstName = ""
                var lastName = ""
                val gender = ""
                val birthday = ""
                var email = ""
                val picture = ""
                val user = result.data
                socialId = user.idStr
                email = user.email
                /*picture = user.profileImageUrlHttps.replace("_normal", "");
            firstName = user.name;
            lastName = user.screenName;*/try {
                    firstName = user.name.split(" ").toTypedArray()[0]
                    lastName = user.name.split(" ").toTypedArray()[1]
                } catch (e: java.lang.Exception) {
                    firstName = user.name
                    lastName = ""
                }
                Log.e(
                    "Twitter",
                    "SocialId: $socialId\tFirstName: $firstName\tLastName: $lastName\tEmail: $email"
                )
            }

            override fun failure(exception: TwitterException) {
                Log.e("Twitter", "Failed to get user data " + exception.message)
            }
        })
    }

}