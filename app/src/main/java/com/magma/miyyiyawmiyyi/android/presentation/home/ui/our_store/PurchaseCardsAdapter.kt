package com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.ItemPurchaseCardBinding
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemCardListener

class PurchaseCardsAdapter :
    ListAdapter<PurchaseCard, PurchaseCardsAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemCardListener<PurchaseCard>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemPurchaseCardBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)

    }

    fun setListener(listener: RecyclerItemCardListener<PurchaseCard>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemPurchaseCardBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: PurchaseCard) {
            binding.item = item

            when (item.status) {
                Const.STATUS_COMPLETED -> {
                    binding.btnAction.visibility = View.VISIBLE
                }
                else -> {
                    binding.btnAction.visibility = View.GONE
                }
            }

            binding.executePendingBindings()
        }

        init {
            binding.btnAction.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onItemCardClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<PurchaseCard>() {
        override fun areItemsTheSame(
            oldItem: PurchaseCard,
            newItem: PurchaseCard
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: PurchaseCard,
            newItem: PurchaseCard
        ): Boolean {
            return newItem._id == oldItem._id
        }
    }


    override fun getItemCount() = currentList.size

}

