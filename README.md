
# OptiSocialLogin
 OptiSocialLogin library provides the simple way to login with social networks such as 
1)	Gmail
2)	Facebook
3)	Instagram
4)	Twitter
5)	Linked In

functionalities:
1)Sign In through Social account and get the Basic profile info such as
•	 Name
•	Profile Url
•	Email
•	id

2)Sign out


## Using OptiSocialLogin from Your App
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
OptiSocialLoginFactory.signIn(this, LoginType.GOOGLE,listener = this)
    }
 
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    OptiSocialLoginFactory.onActivityResult(requestCode, resultCode, data)
}

```
 Remaining Logins
```groovy
>Facebook
OptiSocialLoginFactory.signIn(this, LoginType.FB,listener = this)
Twitter
OptiSocialLoginFactory.signIn(this, LoginType.TWITTER, listener = this)

OptiSocialLoginFactory.signIn(this, LoginType.INSTAGRAM, listener = this)
OptiSocialLoginFactory.signIn(this, LoginType.LINKEDIN, listener = this)
```

## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](),
and we'll get back to you.
