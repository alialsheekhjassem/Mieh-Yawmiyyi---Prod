package com.magma.miyyiyawmiyyi.android.data.remote.requests

import android.os.Parcel
import android.os.Parcelable

class RegisterRequest() : Parcelable {

    var name:String? = null

    var phone:String? = null

    var password:String? = null

    var locale:String? = null

    var firebaseAuthToken:String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        phone = parcel.readString()
        password = parcel.readString()
        locale = parcel.readString()
        firebaseAuthToken = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(password)
        parcel.writeString(locale)
        parcel.writeString(firebaseAuthToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegisterRequest> {
        override fun createFromParcel(parcel: Parcel): RegisterRequest {
            return RegisterRequest(parcel)
        }

        override fun newArray(size: Int): Array<RegisterRequest?> {
            return arrayOfNulls(size)
        }
    }
}