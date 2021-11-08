package com.example.socialloginexample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
            findViewById<TextView>(R.id.token).setText("TOKEN: ${token}")

        }


    }
}