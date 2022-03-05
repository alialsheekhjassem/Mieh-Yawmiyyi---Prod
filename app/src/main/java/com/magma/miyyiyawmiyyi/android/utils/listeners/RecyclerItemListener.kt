package com.magma.miyyiyawmiyyi.android.utils.listeners

interface RecyclerItemListener<T> {

    fun onItemClicked(item : T, index : Int)
}