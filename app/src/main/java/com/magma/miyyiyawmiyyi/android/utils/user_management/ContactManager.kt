package com.magma.miyyiyawmiyyi.android.utils.user_management

import android.util.Log
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import com.magma.miyyiyawmiyyi.android.model.Account

class ContactManager {

    private var account: MyAccountResponse? = null
    private var info: InfoResponse? = null
    private var giftCode: String? = null

    init {
        Log.i("ContactManager","Initiating")
        Log.i("ContactManager", "Current MyAccountResponse: ${account?.account}")
    }

    companion object {

        private var instance = ContactManager()

        fun setAccount(account: MyAccountResponse){
            Log.i("ContactManager", "Add Contact ...")
            instance.account = account
        }

        fun setAccount(account: Account){
            Log.i("ContactManager", "Add Contact ...")
            instance.account?.account = account
        }

        fun getCurrentAccount(): MyAccountResponse? {
            return instance.account
        }

        fun setInfo(info: InfoResponse){
            Log.i("ContactManager", "Add InfoResponse ...$info")
            instance.info = info
        }

        fun getCurrentInfo(): InfoResponse? {
            return instance.info
        }

        fun setGiftCode(giftCode: String?){
            Log.i("ContactManager", "Add giftCode ...$giftCode")
            instance.giftCode = giftCode
        }

        fun getGiftCode(): String? {
            return instance.giftCode
        }

        fun refreshInstance() {
            Log.i("ContactManager", "Refresh Manager ...")
            instance = ContactManager()
        }
    }

}