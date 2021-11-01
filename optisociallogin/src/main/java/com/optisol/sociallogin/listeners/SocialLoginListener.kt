package com.optisol.sociallogin.listeners

import android.content.Intent
import com.optisol.sociallogin.listeners.LoginResultListener

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
