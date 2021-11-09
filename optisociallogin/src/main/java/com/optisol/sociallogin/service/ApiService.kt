package com.optisol.sociallogin.service

import com.optisol.sociallogin.model.AccessToken
import com.optisol.sociallogin.model.LinkednUserDetals
import com.optisol.sociallogin.model.UserDetails
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("oauth/access_token")
    fun getInstagramAccessToken(@FieldMap parameters: Map<String, String>): Call<AccessToken>
    //@FieldMap Map<String, String> parameters
    @GET("v12.0/{user_id}")
    fun getUserName(
        @Path("user_id") userId: String?, @Query("access_token") accessToken: String?, @Query(
            "fields"
        ) fields: String?
    ): Call<UserDetails>
    @FormUrlEncoded
    @POST("oauth/v2/accessToken")
    fun getLinkedInAccessToken(@FieldMap parameters: Map<String, String>): Call<AccessToken>

    @GET("v2/me")
    fun getLinkedInProfile(
       // @Query("oauth2_access_token") accessToken: String?,
        @Header("Authorization") accessToken: String?,
        @Query("projection") projectionStr: String?
    ): Call<LinkednUserDetals>?
}