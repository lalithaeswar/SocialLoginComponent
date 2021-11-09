package com.optisol.sociallogin.activities

import android.net.Uri
import android.util.Log
import android.view.View
import com.optisol.sociallogin.OptiSocialLoginFactory
import com.optisol.sociallogin.R
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.model.AccessToken
import com.optisol.sociallogin.model.Constant
import com.optisol.sociallogin.model.LoginResult
import com.optisol.sociallogin.model.UserDetails
import com.optisol.sociallogin.service.ServiceBuilder
import retrofit2.Response


class InstaSigninActivity:SignInActivity(){

    override fun initValues() {
        pagenotloaded=Constant.PAGE_CANT_LOADING_MESSAGE
        pagecancelled=Constant.SIGNIN_CANCELED_MESSAGE
        clientId=getString(R.string.client_id)
        clientSecret=getString(R.string.client_secret_key)
        redirectUri=getString(R.string.callback_url)
        initWebView()
    }
    private  fun getProfileData(accessToken: AccessToken){
        val call = ServiceBuilder.buildService(Constant.LINKEDIN_GRAPH_URL).getUserName(accessToken.userId.toString(),accessToken.accessToken,"username,id")
        call.enqueue(object : retrofit2.Callback<UserDetails>{
            override fun onFailure(call: retrofit2.Call<UserDetails>, t: Throwable) {
                finish()
            }
            override fun onResponse(
                call: retrofit2.Call<UserDetails>,
                response: Response<UserDetails>
            ) {
                if(response.isSuccessful) {
                    val userDetails = response.body()
                    handleSignInResult(userDetails)

                }else{
                    finish()
                    OptiSocialLoginFactory.listener?.onFailureLogin("Instagram login failed")
                }
            }

        })

    }
    private fun handleSignInResult(account: UserDetails?) {
        val result = LoginResult(
            true,
            LoginType.INSTAGRAM,
            id = account?.id,
            email = "",
            firstName = account?.username,
            lastName = account?.username,
            token = account?.id,
            // avatar = if (account.getPhotoUrl() != null) account.photoUrl.toString() else ""

        )
        finish()
        OptiSocialLoginFactory.listener?.onSuccessLogin(result)


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

        val call = ServiceBuilder.buildService(Constant.INSTRA_BASE_URL).getInstagramAccessToken(inputs)
        call.enqueue(object : retrofit2.Callback<AccessToken> {
            override fun onFailure(call: retrofit2.Call<AccessToken>, t: Throwable) {
                finish()
            }

            override fun onResponse(
                call: retrofit2.Call<AccessToken>,
                response: Response<AccessToken>
            ) {
                if(response.isSuccessful) {
                    val accessToken = response.body()
                    accessToken?.let{
                        isNotLoggedIn = false
                        getProfileData(accessToken)}

                }else{
                    finish()
                    OptiSocialLoginFactory.listener?.onFailureLogin("Instagram login failed")
                }
            }

        })
    }

    override fun generateUrl(): String {
        return Uri.parse(Constant.AUTHORIZATION_URL)
            .buildUpon()
            .appendQueryParameter(Constant.RESPONSE_TYPE, Constant.CODE)
            .appendQueryParameter(Constant.CLIENT_ID, this.clientId)
            .appendQueryParameter(Constant.REDIRECT_URI, this.redirectUri)
            .appendQueryParameter(Constant.SCOPE, "user_profile,user_media").build().toString()
    }



}


