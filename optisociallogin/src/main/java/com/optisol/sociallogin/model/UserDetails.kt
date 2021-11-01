package com.optisol.sociallogin.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class UserDetails {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null
}