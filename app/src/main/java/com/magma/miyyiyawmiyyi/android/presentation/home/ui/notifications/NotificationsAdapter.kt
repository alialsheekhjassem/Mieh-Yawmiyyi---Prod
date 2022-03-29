package com.magma.miyyiyawmiyyi.android.presentation.home.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.databinding.ItemNotificationBinding
import com.magma.miyyiyawmiyyi.android.model.Notification
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener

class NotificationsAdapter :
    ListAdapter<Notification, NotificationsAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemListener<Notification>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemNotificationBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)

    }

    fun setListener(listener: RecyclerItemListener<Notification>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Notification) {
            binding.item = item
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(
            oldItem: Notification,
            newItem: Notification
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: Notification,
            newItem: Notification
        ): Boolean {
            return newItem._id == oldItem._id
        }
    }


    override fun getItemCount() = currentList.size

}

