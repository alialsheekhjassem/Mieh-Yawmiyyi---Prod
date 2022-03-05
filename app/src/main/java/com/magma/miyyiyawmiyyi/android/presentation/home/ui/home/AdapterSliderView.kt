package com.magma.miyyiyawmiyyi.android.presentation.home.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import com.magma.miyyiyawmiyyi.android.databinding.ItemImageSliderBinding
import com.magma.miyyiyawmiyyi.android.model.Image
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.home.AdapterSliderView.ViewModelItemSlider
import java.util.ArrayList

class AdapterSliderView : SliderViewAdapter<ViewModelItemSlider>() {

    private var mSliderItems: List<Image>

    fun setImages(sliderItems: List<Image>) {
        mSliderItems = sliderItems
        Log.d("TAG", "setImages: $sliderItems")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewModelItemSlider {
        val binding = ItemImageSliderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("TAG", "onCreateViewHolder: $binding")
        return ViewModelItemSlider(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewModelItemSlider, position: Int) {
        viewHolder.onBind(position)
    }

    override fun getCount() = mSliderItems.size

    inner class ViewModelItemSlider(private val mBinding: ItemImageSliderBinding) :
        ViewHolder(mBinding.root) {
        fun onBind(position: Int) {
            val image = mSliderItems[position]
            Log.d("TAG", "EEE onBind: $image")
            mBinding.imageUrl = image.path
        }
    }

    init {
        mSliderItems = ArrayList()
    }
}