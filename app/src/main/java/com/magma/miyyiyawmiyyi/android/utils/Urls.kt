package com.magma.miyyiyawmiyyi.android.utils

class Urls {

    companion object{
        const val NEWS = "/v2/everything/"
        const val NEARBY_PLACES = "/maps/api/place/nearbysearch/json"
        const val TEXT_SEARCH = "/maps/api/place/textsearch/json"

        const val END_POINT_LOGIN = "/api/v1/auth/login"
        const val END_POINT_LOGOUT = "/api/v1/auth/logout"
        const val END_POINT_REGISTER = "/api/auth/register"
        const val END_POINT_RESET_PASSWORD = "/api/auth/resetPassword"
        const val END_POINT_TASKS = "/api/v1/tasks"
        const val END_POINT_GIFT_STORE = "/api/v1/gift_store"
        const val END_POINT_GIFT_STORE_PURCHASES = "/api/v1/gift_store/purchase"
        const val END_POINT_INBOX = "/api/v1/accounts/inbox"
        const val END_POINT_ROUNDS = "/api/v1/rounds"
        const val END_POINT_MY_ACCOUNT = "/api/v1/accounts/my"
        const val END_POINT_UPDATE_MY_ACCOUNT = "/api/v1/accounts"
        const val END_POINT_TICKETS = "/api/v1/tickets"
        const val END_POINT_INFO = "/api/v1/accounts/info"
        const val END_POINT_CREATE_PURCHASE = "/api/v1/gift_store/{gift}/purchase"
        const val END_POINT_MARK_AS_DONE = "/api/v1/tasks"
        const val END_POINT_ROUND_STATISTICS = "/api/v1/rounds/statistics"
        const val END_POINT_COUNTRIES = "/api/v1/countries"
        const val END_POINT_GIFT_CODE = "/api/v1/gift_store/purchase/code"
    }
}