
# OptiSocialLogin
 OptiSocialLogin library provides the simple way to login with social networks such as 
1)	Gmail
2)	Facebook
3)	Instagram
4)	Twitter
5)	Linked In

functionalities:
1) Sign In through Social account and get the Basic profile info such as
- Name
- avatar
-	Email
- id

2) Sign out


## Using OptiSocialLogin from Your App
### Basic Setup
1) [Google](https://developers.google.com/identity/sign-in/android/start-integrating)
2) [FaceBook](https://developers.facebook.com/docs/facebook-login/android)
3) [Twitter](https://developer.twitter.com/en/portal/dashboard)
4) [Instagram](https://developers.facebook.com/docs/instagram-basic-display-api/getting-started)
5) [LinkedIn](https://www.linkedin.com/developers/apps )

Add Resources in String.xml
```groovy
<string name="callback_url">https://www.xxxxxxxxxxx.com</string> //it should be same as mentioned in developer console(Instagram ,LinkedIn)
//Facebook
<string name="facebook_app_id">xxxxxxxxxxxxxxxxx</string>
<string name="fb_login_protocol_scheme">xxxxxxxxxxxxxxxxxxx</string>
//Twitter
<string name="twitter_consumer_key">xxxxx</string>
 <string name="twitter_consumer_secret">xxxxxxxx</string>
<string name="twitter_callback_url">socialloginexample://</string>
//Instagrm
<string name="client_id">xxxxxxxxxxxxxxxxxxxx</string>
<string name="client_secret_key">xxxxxxxxxxxxxxxxxxx</string>
//Linked In
<string name="linkedIn_client_id">xxxxxxxxxxx</string>
<string name="linkedIn_client_secret_key">xxxxxxxxxxxxxxx</string>

 ```
### Specifying Gradle Dependencies

To begin, copy optisociallogin module to your app and add in build.gradle dependencies section:
```groovy
implementation project(':optisociallogin') 
settings.gradle
 include ':optisociallogin'
 ```
 ## Usage
 
Easy Login with Google,Facebook,Twitter,Instagram,LinkedIn

Login with Google
```groovy
fun loginWithGoogle() {
OptiSocialLoginFactory.signIn(this, LoginType.GOOGLE,listener)
    }
 
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    OptiSocialLoginFactory.onActivityResult(requestCode, resultCode, data)
}
 var listener=object:LoginResultListener{
        override fun onSuccessLogin(loginResult: LoginResult) {
            var firstName=loginResult.firstName
            var lastName=loginResult.lastName
            var avatar=loginResult.avatar
            var email=loginResult.email
            var id=loginResult.id
        }
        override fun onFailureLogin(error: String) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }
```
 Remaining Logins
```groovy
//Facebook
OptiSocialLoginFactory.signIn(this, LoginType.FB,listener)
//Twitter
OptiSocialLoginFactory.signIn(this, LoginType.TWITTER, listener)
//Instagram
OptiSocialLoginFactory.signIn(this, LoginType.INSTAGRAM, listener)
//LinkedIn
OptiSocialLoginFactory.signIn(this, LoginType.LINKEDIN, listener)
```

## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](https://github.com/lalithaeswar/SocialLoginComponent/issues),
and we'll get back to you.
