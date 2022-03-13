package com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.databinding.ItemGiftCardBinding
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener

class GiftCardsAdapter :
    ListAdapter<GiftCard, GiftCardsAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemListener<GiftCard>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemGiftCardBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)

    }

    fun setListener(listener: RecyclerItemListener<GiftCard>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemGiftCardBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: GiftCard) {
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

    class NewsDiffCallBacks : DiffUtil.ItemCallback<GiftCard>() {
        override fun areItemsTheSame(
            oldItem: GiftCard,
            newItem: GiftCard
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: GiftCard,
            newItem: GiftCard
        ): Boolean {
            return newItem._id == oldItem._id
        }
    }


    override fun getItemCount() = currentList.size

}

