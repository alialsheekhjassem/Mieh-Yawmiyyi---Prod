package com.magma.miyyiyawmiyyi.android.presentation.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magma.miyyiyawmiyyi.android.databinding.TestBinding
import com.magma.miyyiyawmiyyi.android.model.Restaurant
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemFoodListener

class FoodAdapter :
    ListAdapter<Restaurant, FoodAdapter.MyViewHolder>(NewsDiffCallBacks()) {

    lateinit var context: Context
    //var dynamicView = false

    private lateinit var listener: RecyclerItemFoodListener<Restaurant>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val binding = TestBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    fun setListener(listener: RecyclerItemFoodListener<Restaurant>) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)

        /*Glide.with(context).load(currentList[position].imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
            .into(holder.binding.imgRestaurant).getSize { width, height ->
                val aspectRatio = width.toFloat() / height.toFloat()
                holder.binding.imgRestaurant.setAspectRatio(aspectRatio)
                if (dynamicView) {
                    val lp = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    holder.binding.imgRestaurant.layoutParams = lp
                } else {
                    val imageHeight = context.resources.getDimension(R.dimen.image_height)
                    val lp = ConstraintLayout.LayoutParams(MATCH_PARENT, imageHeight.toInt())
                    holder.binding.imgRestaurant.layoutParams = lp
                }
            }*/
        holder.bind(item)
    }

    inner class MyViewHolder(val binding: TestBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Restaurant) {

        }

        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(p0: View?) {
            val item = getItem(bindingAdapterPosition)
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

