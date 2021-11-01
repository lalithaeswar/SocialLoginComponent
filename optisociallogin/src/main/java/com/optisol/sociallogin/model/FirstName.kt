package com.optisol.sociallogin.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class FirstName {
    @SerializedName("localized")
    @Expose
     val localized: Localized? = null

    @SerializedName("preferredLocale")
    @Expose
     val preferredLocale: PreferredLocale? = null
}