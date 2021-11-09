package com.example.socialloginexample

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.optisol.sociallogin.OptiSocialLoginFactory
import com.optisol.sociallogin.helper.LoginType


class HomeActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        getIntentValues()
    }
    private  fun getIntentValues(){
        val inten=intent.getBundleExtra("result")
        inten?.let{
            val firstName=inten.getString("username")
            val email=inten.getString("email")
            val token=inten.getString("token")
            var avatar=inten.getString("photourl")
            findViewById<TextView>(R.id.username).setText("USERNAME : ${firstName}")
            findViewById<TextView>(R.id.email).setText("EMAIL : ${email}")
            findViewById<TextView>(R.id.token).setText("ID: ${token}")
           val loginType=inten.getString("logintype")
            findViewById<TextView>(R.id.signOut_social).setOnClickListener {
              loginType?.let{
                  OptiSocialLoginFactory.logout(this, LoginType.valueOf(loginType))
                  finish()
              }
            }
        }



    }

}