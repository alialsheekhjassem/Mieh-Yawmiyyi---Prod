package com.magma.miyyiyawmiyyi.android.utils.listeners

interface RecyclerItemCartListener<T> {

    fun onItemClicked(item : T, index : Int)

    fun onIncClicked(item : T, index : Int)

    fun onDecClicked(item : T, index : Int)
}