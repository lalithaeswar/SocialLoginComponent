package com.optisol.sociallogin.activities

import android.app.Activity
import android.net.Uri
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
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.model.*
import com.optisol.sociallogin.service.ServiceBuilder
import retrofit2.Response

class LinkedInSigninActivity:Activity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private var clientId: String? = null
    private var clientSecret: String? = null
    private var redirectUri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.web_view)
        progressBar = findViewById(R.id.progress_bar)
        webView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        clientId=getString(R.string.linkedIn_client_id)
        clientSecret=getString(R.string.linkedIn_client_secret_key)
        redirectUri=getString(R.string.callback_url)
        initWebView()
    }


    private fun initWebView() {
        val url = generateUrl()
        Log.e("url", url)
        // clearCookieBeforeAuthorization()
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
                if (request.getUrl().toString().startsWith(redirectUri!!)) {
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
                OnFailure(  Constant.PAGE_CANT_LOADING_MESSAGE)

            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                OnFailure(Constant.PAGE_CANT_LOADING_MESSAGE)
            }

        }

        webView.loadUrl(url)
    }

    private fun handleUrl(url: String) {
        if (url.contains("code")) {
            val temp = url.split("=").toTypedArray()
            val code = temp[1]
            getAccessToken(code)
        } else if (url.contains("error")) {
            val temp = url.split("=").toTypedArray()
            Log.e("ERROR", "Login error: " + temp[temp.size - 1])
        }
    }
    private  fun getprofileData(acessToken: AccessToken){
        val call = ServiceBuilder.buildService(Constant.LINKEDIN_BASE_URL).getLinkedInProfile("Bearer "+acessToken.accessToken,Constant.projectionStr)
        call?.enqueue(object : retrofit2.Callback<LinkednUserDetals>{
            override fun onFailure(call: retrofit2.Call<LinkednUserDetals>, t: Throwable) {
                OnFailure("LinkedIn login failed")
            }

            override fun onResponse(
                call: retrofit2.Call<LinkednUserDetals>,
                response: Response<LinkednUserDetals>
            ) {
                if(response.isSuccessful) {
                    val userDetails = response.body()
                  //  Log.e("auth", userDetails?.:"")
                    userDetails?.let{handleSignInResult(userDetails)}

                }else{
                    OnFailure("LinkedIn login failed")
                }
            }

        })

    }
    private fun handleSignInResult(account: LinkednUserDetals) {
        if (account!= null) {
            val result = LoginResult(
                true,
                LoginType.INSTAGRAM,
                id = account.id,
                email = "",
                firstName = account.firstName?.localized?.enUS,
                lastName = account.lastName?.localized?.enUS,
                token = account.id,
                // avatar = if (account.getPhotoUrl() != null) account.photoUrl.toString() else ""

            )
            finish()
            OptiSocialLoginFactory.listener?.onSuccessLogin(result)

        }
    }
    fun getAccessToken(authCode: String) {
        progressBar.visibility = View.VISIBLE
        webView.visibility = View.GONE
        Log.e("auth", authCode)
        val inputs: Map<String, String> = hashMapOf(
            Constant.GRANT_TYPE to Constant.AUTHORIZATION_CODE,
            Constant.CODE to authCode,
            Constant.REDIRECT_URI to this.redirectUri!!,
            Constant.CLIENT_ID to this.clientId!!,
            Constant.CLIENT_SECRET to this.clientSecret!!
        )

        val call = ServiceBuilder.buildService(Constant.LINKEDIN_BASE_URL).getLinkedInAccessToken(inputs)


        call.enqueue(object : retrofit2.Callback<AccessToken> {
            override fun onFailure(call: retrofit2.Call<AccessToken>, t: Throwable) {
                finish()
                Log.e("auth", "error")
            }

            override fun onResponse(
                call: retrofit2.Call<AccessToken>,
                response: Response<AccessToken>
            ) {
                if(response.isSuccessful) {
                    val accessToken = response.body()
                    Log.e("auth", accessToken?.accessToken?:"")
                    accessToken?.let{ getprofileData(accessToken)}

                }else{
                    OnFailure("LinkedIn login failed")
                }
            }

        })
    }

    private fun generateUrl(): String {
        return Uri.parse(Constant.LinkedIn_ACCESS_TOKEN_URL)
            .buildUpon()
            .appendQueryParameter(Constant.RESPONSE_TYPE, Constant.CODE)
            .appendQueryParameter(Constant.CLIENT_ID, this.clientId)
            .appendQueryParameter(Constant.REDIRECT_URI, this.redirectUri)
            .appendQueryParameter(Constant.SCOPE, "r_liteprofile,r_emailaddress").build().toString()
    }
    private fun OnFailure(data:String){
        finish()
        OptiSocialLoginFactory.listener?.onFailureLogin(data)
    }
}



