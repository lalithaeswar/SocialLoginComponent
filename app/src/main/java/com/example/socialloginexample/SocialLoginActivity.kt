package com.example.socialloginexample

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.optisol.sociallogin.*
import com.optisol.sociallogin.activities.InstaSigninActivity
import com.optisol.sociallogin.helper.LoginType
import com.optisol.sociallogin.listeners.LoginResultListener
import com.optisol.sociallogin.model.LoginResult
import java.security.MessageDigest


@Suppress("DEPRECATION")
class SocialLoginActivity  : AppCompatActivity() , View.OnClickListener, LoginResultListener {
    private lateinit var gSignin: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.social_login)
        getHashkey()

        gSignin = findViewById<ImageView>(R.id.gmail)
        gSignin.setOnClickListener(this)
        findViewById<ImageView>(R.id.facebook).setOnClickListener {
            OptiSocialLoginFactory.signIn(this, LoginType.FB, listener = this)
        }
        findViewById<ImageView>(R.id.instagram).setOnClickListener {
            OptiSocialLoginFactory.signIn(this, LoginType.INSTAGRAM, listener = this)
        }
        findViewById<TextView>(R.id.signOut).setOnClickListener {
            OptiSocialLoginFactory.logout(this, loginType = LoginType.FB)
        }
        findViewById<ImageView>(R.id.LinkedIn).setOnClickListener {
            OptiSocialLoginFactory.signIn(this, LoginType.LINKEDIN, listener = this)
        }
        findViewById<ImageView>(R.id.twitter).setOnClickListener {
            OptiSocialLoginFactory.signIn(this, LoginType.TWITTER, listener = this)
        }
}
    override fun onClick(v: View?) {
      OptiSocialLoginFactory.signIn(this, LoginType.GOOGLE, listener = this)
    }

    override fun onSuccessLogin(loginResult: LoginResult) {
        findViewById<TextView>(R.id.result).text=loginResult.toString()
        val bundle=Bundle()
        bundle.putString("username",loginResult.firstName)
        bundle.putString("email",loginResult.email)
        bundle.putString("token",loginResult.token)
        bundle.putString("photourl",loginResult.avatar)
        val loginActivity = Intent(this, HomeActivity::class.java)
        loginActivity.putExtra("result",bundle)
      startActivity(loginActivity)
//        Toast.makeText(this, "Suceess${longResult.toString()} ", Toast.LENGTH_LONG).show()
    }

    override fun onFailureLogin(error: String) {
        Toast.makeText(this, "failure", Toast.LENGTH_LONG).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        OptiSocialLoginFactory.onActivityResult(requestCode, resultCode, data)
    }
fun getHashkey(){
    try {
        val info = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    } catch (e: Exception) {
        Log.e("KeyHash:", e.message.toString())
    }

}





}
