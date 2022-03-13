package com.magma.miyyiyawmiyyi.android.utils.user_management

import android.util.Log
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse

class ContactManager {

    private var account: MyAccountResponse? = null

    init {
        Log.i("ContactManager","Initiating")
        Log.i("ContactManager", "Current MyAccountResponse: ${account?.phone}")
    }

    companion object {

        private var instance = ContactManager()

        fun setAccount(account: MyAccountResponse){
            Log.i("ContactManager", "Add Contact ...")
            instance.account = account
        }

        fun getCurrentAccount(): MyAccountResponse? {
            return instance.account
        }

        fun refreshInstance() {
            Log.i("ContactManager", "Refresh Manager ...")
            instance = ContactManager()
        }
    }

}