package com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.ItemGiftCardGroupBinding
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.model.GiftCardGroup
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemGiftCardListener
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import java.util.*

class GiftCardsGroupAdapter :
    ListAdapter<GiftCardGroup, GiftCardsGroupAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemGiftCardListener<GiftCard>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemGiftCardGroupBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    fun setListener(listener: RecyclerItemGiftCardListener<GiftCard>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: ItemGiftCardGroupBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, RecyclerItemListener<GiftCard> {

        fun bind(item: GiftCardGroup) {
            val adapter = GiftCardsAdapter()
            adapter.setListener(this)
            binding.recyclerGoogleCards.adapter = adapter
            adapter.submitList(item.gifts)
            when (Locale.getDefault().language) {
                "en" -> {
                    binding.txtTitle.text = item.title?.en
                    binding.txtBody.text = String.format(
                        binding.root.context.getString(R.string.exchange_your_points_with_google_gift_cards),
                        item.title?.en
                    )
                }
                "ar" -> {
                    binding.txtTitle.text = item.title?.ar
                    binding.txtBody.text = String.format(
                        binding.root.context.getString(R.string.exchange_your_points_with_google_gift_cards),
                        item.title?.ar
                    )
                }
            }
            binding.executePendingBindings()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onGroupClicked(getItem(bindingAdapterPosition), bindingAdapterPosition)
        }

        override fun onItemClicked(item: GiftCard, index: Int) {
            listener.onItemClicked(item, index)
        }
    }

    class NewsDiffCallBacks : DiffUtil.ItemCallback<GiftCardGroup>() {
        override fun areItemsTheSame(
            oldItem: GiftCardGroup,
            newItem: GiftCardGroup
        ): Boolean {
            return oldItem.title?.equals(newItem.title) ?: false
        }

        override fun areContentsTheSame(
            oldItem: GiftCardGroup,
            newItem: GiftCardGroup
        ): Boolean {
            return newItem.title?.equals(oldItem.title) ?: false
        }
    }


    override fun getItemCount() = currentList.size

}

