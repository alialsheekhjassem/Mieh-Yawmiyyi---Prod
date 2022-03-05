package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.databinding.ItemTicketBinding
import com.magma.miyyiyawmiyyi.android.model.Winner
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener

class TicketsAdapter :
    ListAdapter<Winner, TicketsAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemListener<Winner>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemTicketBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)

    }

    fun setListener(listener: RecyclerItemListener<Winner>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Winner) {
            binding.item = item
            binding.txtOrder.text = (bindingAdapterPosition+1).toString()
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<Winner>() {
        override fun areItemsTheSame(
            oldItem: Winner,
            newItem: Winner
        ): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(
            oldItem: Winner,
            newItem: Winner
        ): Boolean {
            return newItem.type == oldItem.type
        }
    }


    override fun getItemCount() = currentList.size

}

