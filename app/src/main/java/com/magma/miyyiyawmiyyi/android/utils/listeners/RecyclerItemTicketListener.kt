package com.magma.miyyiyawmiyyi.android.utils.listeners

interface RecyclerItemTicketListener<T> {

    fun onItemClicked(item : T, index : Int)

    fun onGetItNowClicked(item : T, index : Int)
}