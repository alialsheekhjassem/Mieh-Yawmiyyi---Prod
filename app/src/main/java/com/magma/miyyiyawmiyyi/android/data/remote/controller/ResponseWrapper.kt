package com.magma.miyyiyawmiyyi.android.data.remote.controller

data class ResponseWrapper<T>(val status : Int, val failureMessage:String, val successResult: T)