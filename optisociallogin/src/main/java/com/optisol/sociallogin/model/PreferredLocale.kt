package com.optisol.sociallogin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PreferredLocale {
    @SerializedName("country")
    @Expose
     val country: String? = null

    @SerializedName("language")
    @Expose
     val language: String? = null

}