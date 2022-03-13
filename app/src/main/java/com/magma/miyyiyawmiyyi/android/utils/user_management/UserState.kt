package com.magma.miyyiyawmiyyi.android.utils.user_management

enum class UserState {
    NEW, ON_BOARDING, LANDLORD, LEASE;

    companion object {
        fun isMember(value: String): Boolean {
            return (value == NEW.name || value == ON_BOARDING.name || value == ON_BOARDING.name || value == ON_BOARDING.name)
        }
    }

}