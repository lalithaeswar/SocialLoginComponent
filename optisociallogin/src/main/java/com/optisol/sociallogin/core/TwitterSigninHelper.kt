package com.optisol.sociallogin.core

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.optisol.sociallogin.OptiSocialLoginFactory
import com.optisol.sociallogin.R
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.helper.clearCookies
import com.optisol.sociallogin.listeners.LoginResultListener
import com.optisol.sociallogin.listeners.SocialLoginListener
import com.optisol.sociallogin.model.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder


class TwitterSigninHelper(var activity: Activity): SocialLoginListener {

    private val TWITTER_KEY =activity.getString(R.string.twitter_consumer_key)
    private val TWITTER_SECRET =  activity.getString(R.string.twitter_consumer_secret)
  private val CALLBACK_URL=activity.getString(R.string.twitter_callback_url)
    lateinit var twitter: Twitter
    lateinit var twitterDialog: Dialog
    var accToken: AccessToken? = null
    var listener: LoginResultListener?=null

    override fun signIn() {
        GlobalScope.launch(Dispatchers.Default) {
            val builder = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_KEY)
                .setOAuthConsumerSecret( TWITTER_SECRET )
                .setIncludeEmailEnabled(true)
            val config = builder.build()
            val factory = TwitterFactory(config)
            twitter = factory.instance
            try {
                val requestToken = twitter.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    setupTwitterWebviewDialog(requestToken.authorizationURL)
                }
            } catch (e: IllegalStateException) {
                Log.e("ERROR: ", e.toString())
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebviewDialog(url: String) {
        clearCookies(activity)
        twitterDialog = Dialog(activity)
        val webView = WebView(activity)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.clearCache(true)
        webView.clearHistory()
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class TwitterWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(CALLBACK_URL)) {
                Log.d("Authorization URL: ", request?.url.toString())
                handleUrl(request?.url.toString())

                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString().contains(CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(CALLBACK_URL)) {
                Log.d("Authorization URL: ", url)
                handleUrl(url)

                // Close the dialog after getting the oauth_verifier
                if (url.contains(CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            twitterDialog.dismiss()
            listener?.onFailureLogin(error = error?.description.toString())
        }

        // Get the oauth_verifier
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
            GlobalScope.launch(Dispatchers.Main) {
                accToken = withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
                getUserProfile()
            }
        }
    }


    suspend fun getUserProfile() {
    val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
        val result = LoginResult(
            true,
            LoginType.INSTAGRAM,
            id = usr.id.toString(),
            email = usr.email,
            firstName = usr.name,
            lastName = usr.screenName,
            token = accToken?.token,
            avatar =  usr.profileImageURLHttps.replace("_normal", "")
            // avatar = if (account.getPhotoUrl() != null) account.photoUrl.toString() else ""

        )

      listener?.onSuccessLogin(result)


}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun setResultListener(listener: LoginResultListener) {
        this.listener=listener
    }




}