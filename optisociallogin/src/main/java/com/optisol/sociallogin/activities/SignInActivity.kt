package com.optisol.sociallogin.activities

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.optisol.sociallogin.OptiSocialLoginFactory
import com.optisol.sociallogin.R
import com.optisol.sociallogin.helper.clearCookies
import com.optisol.sociallogin.model.Constant

abstract class SignInActivity :Activity(){
    lateinit var webView: WebView
  lateinit var progressBar: ProgressBar
  lateinit var pagenotloaded:String
  lateinit var pagecancelled:String
    var clientId: String? = null
   var clientSecret: String? = null
     var redirectUri: String? = null
    var isNotLoggedIn = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.web_view)
        progressBar = findViewById(R.id.progress_bar)
        webView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
       initValues()
    }
    fun handleUrl(url: String) {
        if (url.contains("code")) {
            val temp = url.split("=").toTypedArray()
            val code = temp[1]
            getAccessToken(code)
        } else if (url.contains("error")) {
            val temp = url.split("=").toTypedArray()
            onFailure(temp[temp.size - 1])
        }
    }
    abstract fun initValues()
    abstract fun getAccessToken(token:String)
    abstract fun generateUrl():String
    fun onFailure(data:String){
        finish()
        OptiSocialLoginFactory.listener?.onFailureLogin(data)
    }
    fun initWebView() {
        val url = generateUrl()
        Log.e("url", url.toString())
        clearCookies(this)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith(redirectUri!!)) {
                    handleUrl(url)
                    return true
                }
                return false
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (request.url.toString().startsWith(redirectUri!!)) {
                    handleUrl(request.url.toString())
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
                onFailure(
                    pagenotloaded
                )
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                onFailure(
                  pagenotloaded
                )
            }

        }
        webView.clearCache(true)
        webView.clearHistory()
        webView.loadUrl(url)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (isNotLoggedIn) {
            runOnUiThread {
                onFailure(pagecancelled)
            }
        }
    }

}