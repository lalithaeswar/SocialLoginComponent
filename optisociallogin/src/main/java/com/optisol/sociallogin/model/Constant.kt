package com.optisol.sociallogin.model

class Constant {
    companion object {
         const val INSTRA_BASE_URL="https://api.instagram.com/"
         const val INSTRA_GRAPH_URL="https://graph.instagram.com/"
         const val LINKEDIN_BASE_URL="https://api.linkedin.com/"
         const val LINKEDIN_GRAPH_URL="https://graph.instagram.com/"
         const val AUTHORIZATION_URL = "https://api.instagram.com/oauth/authorize/"
         const val LinkedIn_ACCESS_TOKEN_URL = "https://www.linkedin.com/oauth/v2/authorization"

         // Success
         const val projectionStr = "(id,firstName,lastName,profilePicture(displayImage~:playableStreams))"
         const val RESPONSE_TYPE = "response_type"
         const val CLIENT_ID = "client_id"
         const val REDIRECT_URI = "redirect_uri"
         const val STATE = "state"
         const val SCOPE = "scope"
         const val CODE = "code"
         const val ERROR = "error"
         const val ERROR_DESCRIPTION = "error_description"
         const val GRANT_TYPE = "grant_type"
         const val AUTHORIZATION_CODE = "authorization_code"
         const val CLIENT_SECRET = "client_secret"

         const val PAGE_CANT_LOADING_MESSAGE = "Instagram cannot be loaded"
         const val SIGNIN_CANCELED_MESSAGE = "Instagram  sign in has been canceled"
         const val LINKED_PAGE_CANT_LOADING = "LinkedIn cannot be loaded"
         const val LINKEDIN_CANCELED_MESSAGE = "LinkedIn  sign in has been canceled"
         const val FACEBOOK_CANCELLED="Facebook  sign in has been canceled"
    }
}