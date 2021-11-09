package com.optisol.sociallogin.activities

import android.net.Uri
import android.util.Log
import android.view.View
import com.optisol.sociallogin.OptiSocialLoginFactory
import com.optisol.sociallogin.R
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.model.AccessToken
import com.optisol.sociallogin.model.Constant
import com.optisol.sociallogin.model.LinkednUserDetals
import com.optisol.sociallogin.model.LoginResult
import com.optisol.sociallogin.service.ServiceBuilder
import retrofit2.Response

@Suppress("DEPRECATION")
class LinkedInSigninActivity:SignInActivity() {


    override fun initValues() {
        pagenotloaded=Constant.LINKED_PAGE_CANT_LOADING
        pagecancelled=Constant.LINKEDIN_CANCELED_MESSAGE
        clientId=getString(R.string.linkedIn_client_id)
        clientSecret=getString(R.string.linkedIn_client_secret_key)
        redirectUri=getString(R.string.callback_url)
        initWebView()
    }

    /* @SuppressLint("SetJavaScriptEnabled")
     private fun initWebView() {
         val url = generateUrl()
         Log.e("url", url)
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
                 onFailure(  Constant.PAGE_CANT_LOADING_MESSAGE)

             }

             override fun onReceivedError(
                 view: WebView?,
                 errorCode: Int,
                 description: String?,
                 failingUrl: String?
             ) {
                 super.onReceivedError(view, errorCode, description, failingUrl)
                 onFailure(Constant.PAGE_CANT_LOADING_MESSAGE)
             }

         }

         webView.loadUrl(url)
     }

 */
    private  fun getProfileData(acessToken: AccessToken){
        val call = ServiceBuilder.buildService(Constant.LINKEDIN_BASE_URL).getLinkedInProfile("Bearer "+acessToken.accessToken,Constant.projectionStr)
        call?.enqueue(object : retrofit2.Callback<LinkednUserDetals>{
            override fun onFailure(call: retrofit2.Call<LinkednUserDetals>, t: Throwable) {
                finish()
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
                    onFailure("LinkedIn login failed")
                }
            }

        })

    }
    private fun handleSignInResult(account: LinkednUserDetals?) {
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
    override fun getAccessToken(token: String) {
        progressBar.visibility = View.VISIBLE
        webView.visibility = View.GONE
        Log.e("auth", token)
        val inputs: Map<String, String> = hashMapOf(
            Constant.GRANT_TYPE to Constant.AUTHORIZATION_CODE,
            Constant.CODE to token,
            Constant.REDIRECT_URI to this.redirectUri!!,
            Constant.CLIENT_ID to this.clientId!!,
            Constant.CLIENT_SECRET to this.clientSecret!!
        )

        val call = ServiceBuilder.buildService(Constant.LINKEDIN_BASE_URL).getLinkedInAccessToken(inputs)


        call.enqueue(object : retrofit2.Callback<AccessToken> {
            override fun onFailure(call: retrofit2.Call<AccessToken>, t: Throwable) {
                onFailure(t.message?:"Internal server error")
            }

            override fun onResponse(
                call: retrofit2.Call<AccessToken>,
                response: Response<AccessToken>
            ) {
                if(response.isSuccessful) {
                    val accessToken = response.body()
                    Log.e("auth", accessToken?.accessToken?:"")
                    accessToken?.let{
                        isNotLoggedIn = false
                        getProfileData(accessToken)}

                }else{
                    onFailure("LinkedIn login failed")
                }
            }

        })
    }
    override fun generateUrl(): String {
        return Uri.parse(Constant.LinkedIn_ACCESS_TOKEN_URL)
            .buildUpon()
            .appendQueryParameter(Constant.RESPONSE_TYPE, Constant.CODE)
            .appendQueryParameter(Constant.CLIENT_ID, this.clientId)
            .appendQueryParameter(Constant.REDIRECT_URI, this.redirectUri)
            .appendQueryParameter(Constant.SCOPE, "r_liteprofile,r_emailaddress").build().toString()
    }

}



