package com.optisol.sociallogin

import android.content.Intent

interface SocialLoginListener {
   fun signIn()
   fun signout()
   fun onActivityResult(
      requestCode: Int,
      resultCode: Int,
      data: Intent?
   )
   fun setResultListener(listener: LoginResultListener)

}
