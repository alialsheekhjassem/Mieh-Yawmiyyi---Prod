package com.magma.miyyiyawmiyyi.android.utils.listeners

import com.magma.miyyiyawmiyyi.android.model.GiftCardGroup

interface RecyclerItemGiftCardListener<T> {

    fun onGroupClicked(item : GiftCardGroup, index : Int)

    fun onItemClicked(item : T, index : Int)
}