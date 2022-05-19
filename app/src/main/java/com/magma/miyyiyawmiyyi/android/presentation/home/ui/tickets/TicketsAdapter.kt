package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.databinding.ItemGetItNowBinding
import com.magma.miyyiyawmiyyi.android.databinding.ItemTicketBinding
import com.magma.miyyiyawmiyyi.android.model.Ticket
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemTicketListener

class TicketsAdapter :
    ListAdapter<Ticket, RecyclerView.ViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    private lateinit var listener: RecyclerItemTicketListener<Ticket>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)

        return when (viewType) {
            Const.TYPE_GET_IT_NOW_ID -> {
                val binding = ItemGetItNowBinding.inflate(layoutInflater, parent, false)
                GetItNowViewHolder(binding)
            }
            else -> {
                val binding = ItemTicketBinding.inflate(layoutInflater, parent, false)
                MyViewHolder(binding)
            }
        }
    }

    fun setListener(listener: RecyclerItemTicketListener<Ticket>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            Const.TYPE_GET_IT_NOW_ID -> {
                val getItNowHolder = holder as TicketsAdapter.GetItNowViewHolder
                getItNowHolder.bind(item)
            }
            else -> {
                val getItNowHolder = holder as TicketsAdapter.MyViewHolder
                getItNowHolder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item._id == Const.TYPE_GET_IT_NOW) {
            Const.TYPE_GET_IT_NOW_ID
        } else {
            Const.TYPE_TICKET_ID
        }
    }

    inner class MyViewHolder(val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Ticket) {
            binding.item = item
            binding.txtOrder.text = (bindingAdapterPosition + 1).toString()
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }
    }

    inner class GetItNowViewHolder(val binding: ItemGetItNowBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Ticket) {
            binding.item = item
            binding.txtOrder.text = (bindingAdapterPosition + 1).toString()
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onGetItNowClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(
            oldItem: Ticket,
            newItem: Ticket
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: Ticket,
            newItem: Ticket
        ): Boolean {
            return newItem._id == oldItem._id
        }
    }


    override fun getItemCount() = currentList.size

}

