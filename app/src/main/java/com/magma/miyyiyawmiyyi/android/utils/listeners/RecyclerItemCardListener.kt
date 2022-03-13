package com.magma.miyyiyawmiyyi.android.utils.listeners

interface RecyclerItemCardListener<T> {

    fun onItemCardClicked(item : T, index : Int)
}