package com.optisol.sociallogin.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AccessToken {
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Long? = null

    @SerializedName("error_type")
    @Expose
    var errorType: String? = null

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("error_message")
    @Expose
    var errorMessage: String? = null
}