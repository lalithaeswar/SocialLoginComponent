package com.example.socialloginexample


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.optisol.sociallogin.LoginResult
import com.optisol.sociallogin.LoginResultListener
import com.optisol.sociallogin.LoginType
import com.optisol.sociallogin.SocialLogin


class SocialLoginFragment :FragmentActivity(),View.OnClickListener, LoginResultListener {
    fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.social_login, parent, false)
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
    override fun onClick(v: View?) {
        SocialLogin.signIn(this, LoginType.GOOGLE,listener = this)
    }

    override fun onSuccessLogin(longResult: LoginResult) {
        Toast.makeText(this, "Suceess${longResult.toString()} ", Toast.LENGTH_LONG).show()
    }

    override fun onFailureLogin(error: String) {
        Toast.makeText(this, "failure", Toast.LENGTH_LONG).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SocialLogin.onActivityResult(requestCode, resultCode, data)
    }
}