package com.magma.miyyiyawmiyyi.android.presentation.details.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.ItemCartBinding
import com.magma.miyyiyawmiyyi.android.model.Restaurant
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemCartListener

class CartAdapter :
    ListAdapter<Restaurant, CartAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context

    private lateinit var listener: RecyclerItemCartListener<Restaurant>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemCartBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    fun setListener(listener: RecyclerItemCartListener<Restaurant>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Restaurant) {
            binding.item = item
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
            binding.btnInc.setOnClickListener(this)
            binding.btnDec.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val item = getItem(bindingAdapterPosition)
            when (p0?.id) {
                R.id.btn_inc -> {
                    if (item.deletedDate!! < 100) {
                        item.deletedDate = item.deletedDate?.plus(1L)
                    }
                    binding.txtQuantity.text = item.deletedDate.toString()
                    listener.onIncClicked(item, bindingAdapterPosition)
                }
                R.id.btn_dec -> {
                    if (item.deletedDate!! > 0) {
                        item.deletedDate = item.deletedDate?.minus(1L)
                    }
                    binding.txtQuantity.text = item.deletedDate.toString()
                    listener.onDecClicked(item, bindingAdapterPosition)
                }
                else -> {
                    listener.onItemClicked(item, bindingAdapterPosition)
                }
            }
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return oldItem.description == newItem.description
        }

        override fun areContentsTheSame(
            oldItem: Restaurant,
            newItem: Restaurant
        ): Boolean {
            return newItem.description == oldItem.description
        }
    }


    override fun getItemCount() = currentList.size

    fun getItemsCount() : Int {
        var count = 0
        for (item in currentList){
            if (item.deletedDate!! > 0){
                count = count.plus(item.deletedDate!!.toInt())
            }
        }
        return count
    }

}

