package com.optisol.sociallogin.config

import android.app.Application
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig

class OptiCustomApplication:Application(){
    override fun onCreate() {
        super.onCreate()


        Twitter.initialize(this)
    }
}