package com.optisol.sociallogin.listeners

import com.optisol.sociallogin.model.LoginResult

interface LoginResultListener {
    fun onSuccessLogin(longResult: LoginResult)
    fun onFailureLogin(error:String)

}