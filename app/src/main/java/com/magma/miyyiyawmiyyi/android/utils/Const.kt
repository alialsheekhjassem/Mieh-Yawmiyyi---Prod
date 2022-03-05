package com.magma.miyyiyawmiyyi.android.utils

class Const {

    companion object {
        const val DATABASE_NAME: String = "News_Test"
        const val PREF_NAME: String = "ABI_KIRSHK"
        //Network constants
        const val TIMEOUT_CONNECT = 60L   //In seconds
        const val TIMEOUT_READ = 60L   //In seconds
        const val TIMEOUT_WRITE = 60L   //In seconds
        const val PROXIMITY_RADIUS = 10000
        const val API_KEY = "AIzaSyAiAir1uMz3NwJDd9vjIhqeEuTUgw2S7VM"
        const val TYPE_100DOLLAR = "100dollar"
        const val TYPE_GOLDEN_LIRA = "golden_lira"
        const val DATE_FORMAT = "yyyy-MM-dd_HHmmss"

        const val TAG_FoodMenuFragment = "FoodMenuFragment"
        const val TAG_ForgetPasswordFragment = "ForgetPasswordFragment"
        const val TAG_DatePickerParentFragment = "DatePickerParentFragment"
        const val TAG_TimePickerParentFragment = "TimePickerParentFragment"

        //shared pref
        const val PREF_API_TOKEN = "api_token"
        const val PREF_LANG = "lang"
        const val PREF_IS_SHOWN_ONBOARDING = "is_shown_onboarding"

        //Error
        const val ERROR_EMPTY: Int = 1
        const val ERROR_PHONE: Int = 2
        const val ERROR_PASSWORD: Int = 3
        //Error
        const val ERROR_NAME_EMPTY: Int = 1
        const val ERROR_PHONE_EMPTY: Int = 2
        const val ERROR_PHONE_FORMAT: Int = 3
        const val ERROR_PASSWORD_EMPTY: Int = 4
        const val ERROR_PASSWORD_FORMAT: Int = 5
        const val ERROR_CONFIRM_PASSWORD_FORMAT: Int = 6
        const val ERROR_EMAIL_FORMAT: Int = 7

        //Extras
        const val EXTRA_REGISTER_REQUEST = "register_request"
        const val EXTRA_PHONE_NUMBER = "PHONE_NUMBER"

        //TYPES
        const val FACEBOOK_ID = "621b5e4c2fe9e30f26033cd2"
        const val TYPE_SOCIAL_MEDIA = "social_media"

    }
}