package com.optisol.sociallogin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LinkednUserDetals {
    @SerializedName("localizedLastName")
    @Expose
     val localizedLastName: String? = null

    @SerializedName("profilePicture")
    @Expose
     val profilePicture: ProfilePicture? = null

    @SerializedName("firstName")
    @Expose
     val firstName: FirstName? = null

    @SerializedName("lastName")
    @Expose
     val lastName: FirstName? = null

    @SerializedName("id")
    @Expose
     val id: String? = null

    @SerializedName("localizedFirstName")
    @Expose
     val localizedFirstName: String? = null
}