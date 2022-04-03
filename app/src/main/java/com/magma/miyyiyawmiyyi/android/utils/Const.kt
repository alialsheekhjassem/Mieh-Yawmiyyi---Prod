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
        const val INIT_COUNT_DOWN = "00:00:00:00"
        const val HAS_ACTION_KEYWORD: String = "has_action"
        const val NOTIFICATION_KEYWORD: String = "notification"

        const val TAG_FoodMenuFragment = "FoodMenuFragment"
        const val TAG_ForgetPasswordFragment = "ForgetPasswordFragment"
        const val TAG_DatePickerParentFragment = "DatePickerParentFragment"
        const val TAG_TimePickerParentFragment = "TimePickerParentFragment"

        //shared pref
        const val PREF_API_TOKEN = "api_token"
        const val PREF_GIFT_CODE = "gift_code"
        const val PREF_REFRESH_TOKEN = "refresh_token"
        const val PREF_LANG = "lang"
        const val PREF_INVITATION_LINK = "invitation_link"
        const val PREF_IS_SHOWN_ONBOARDING = "is_shown_onboarding"
        const val PREF_IS_GENERAL = "is_general"

        //Error
        const val ERROR_EMPTY: Int = 1
        const val ERROR_PHONE: Int = 2
        const val ERROR_TERMS: Int = 3
        const val ERROR_PASSWORD: Int = 4
        const val ERROR_BIRTHDAY: Int = 5
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
        const val EXTRA_DEEP_LINK = "DEEP_LINK"

        //TYPES
        const val FACEBOOK_ID = "621b5e4c2fe9e30f26033cd2"
        const val INSTAGRAM_ID = "621b5e4c2fe9e30f26033cd1"
        const val YOUTUBE_ID = "621b5e4c2fe9e30f26033cd3"
        const val TYPE_SOCIAL_MEDIA = "social_media"
        const val TYPE_QUIZ = "quiz"
        const val TYPE_AD = "ad"

        //Facebook Ads
        var FB_NATIVE = "1276188569544209_1276229116206821"
        var FB_NATIVE_MEDIUM = "1276188569544209_1276268162869583"
        var FB_BANNER_50 = "xyz11xyz2814xyz_136xyz8767215xyz"
        var FB_BANNER_90 = "xyz11xyz2814xyz_136xyz8767215xyz"
        var FB_RECTANGLE = "xyz11xyz2814xyz_136xyz8767215xyz"
        var FB_INTERSTITIAL = "1276188569544209_1276214102874989"
        var FB_REWARD = "xyz1462xyz62xyz_67xyz3630456xyz"

        //Topics
        const val TOPIC_ROUNDS = "rounds_en"
        const val TOPIC_GRAND_PRIZE = "grand_prize"
        const val TOPIC_GENERAL_AR = "general_ar"
        const val TOPIC_GENERAL_EN = "general_en"

        //STATUS
        const val STATUS_ACTIVE = "active"
        const val STATUS_PENDING = "pending"
        const val STATUS_PROCESSING = "processing"
        const val STATUS_REJECTED = "rejected"
        const val STATUS_COMPLETED = "completed"

        //Notification Type
        const val TYPE_ROUND_ACTIVATE = "\"round_activate\""
        const val TYPE_ROUND_CLOSE = "\"round_close\""
        const val TYPE_ROUND_CANCEL = "\"round_cancel\""
        const val TYPE_ROUND_FINISH = "\"round_finish\""
        const val TYPE_ROUND_UPDATE = "\"update_round\""
        const val TYPE_ROUND_START = "\"round_draw_start\""
        const val TYPE_GRAND_PRIZE_ACTIVATE = "\"grand_prize_activate\""
        const val TYPE_ROUND_TICKET = "\"round_ticket\""
        const val TYPE_GRAND_PRIZE_TICKET = "\"grand_prize_ticket\""
        const val TYPE_ROUND_WINNER = "\"round_winner\""
        const val TYPE_GRAND_PRIZE_WINNER = "\"grand_prize_winner\""
        const val TYPE_GRAND_PRIZE_UPDATE = "\"grand_prize_update\""
        const val TYPE_GRAND_PRIZE_FINISH = "\"grand_prize_finish\""
        const val TYPE_PROCESSING_PURCHASE = "\"processing_purchase\""
        const val TYPE_PURCHASE_REJECTED = "\"rejected_purchase\""
        const val TYPE_GIFT_CODE = "\"gift_code\""
        const val TYPE_GOT_POINTS = "\"got_points\""

        const val NOTIFICATION_TYPE_KEYWORD: String = "type"
        const val NOTIFICATION_ROUND_KEYWORD: String = "round"


        const val TYPE_ROUND_START_DRAW = "fixed_start_draw"
        const val TYPE_ROUND_TICKETS_DRAW = "fixed_tickets_draw"

    }
}