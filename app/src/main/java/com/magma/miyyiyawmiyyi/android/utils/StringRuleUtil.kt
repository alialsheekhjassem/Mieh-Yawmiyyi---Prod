/*
 * Created by Ali Al-Sheekh Jassem on 4/21/21 9:16 PM
 * Copyright (c) 2021 . All rights reserved .
 * Last modified 4/21/21 8:37 PM
 */
package com.magma.miyyiyawmiyyi.android.utils

import android.text.Editable
import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

object StringRuleUtil {
    var NOT_EMPTY_RULE: StringRule = object : StringRule {
        override fun validate(s: Editable?): Boolean {
            return TextUtils.isEmpty(s.toString())
        }
    }
    var EMAIL_RULE: StringRule =
        object : StringRule {
            override fun validate(s: Editable?): Boolean {
                return !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()
            }
        }
    var PHONE_RULE: StringRule =
        object : StringRule {
            override fun validate(s: Editable?): Boolean {
                return !Patterns.PHONE.matcher(s.toString()).matches()
            }
        }
    var PASSWORD_RULE: StringRule = object : StringRule {
        override fun validate(s: Editable?): Boolean {
            return !Pattern.compile(
                "^" +  //"(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        //"(?=.*[a-zA-Z])" +      //any letter
                        //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                        //"(?=\\S+$)" +           //no white spaces
                        ".{8,}" +  //at least 4 characters
                        "$"
            ).matcher(s.toString()).matches()
        }
    }

    interface StringRule {
        fun validate(s: Editable?): Boolean
    }
}