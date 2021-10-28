package com.optisol.sociallogin

interface LoginResultListener {
    fun onSuccessLogin(longResult: LoginResult)
    fun onFailureLogin(error:String)

}