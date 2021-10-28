package com.optisol.sociallogin

import android.os.Parcelable



data class LoginResult(
    val isSuccess: Boolean,
    val loginType: LoginType,
    val id: String? = null,
    val token: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val email: String? = null,
    var errorMessage: String? = null
)
