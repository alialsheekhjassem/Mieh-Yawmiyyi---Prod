package com.magma.miyyiyawmiyyi.android.presentation.details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentFoodMenuBinding
import com.magma.miyyiyawmiyyi.android.model.Restaurant
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemFoodListener
import javax.inject.Inject

class FoodMenuFragment : Fragment(), RecyclerItemFoodListener<Restaurant> {

    private var _binding: FragmentFoodMenuBinding? = null
    private lateinit var navController: NavController
    private var lytShowCart: LinearLayoutCompat? = null
    private var txtItemsCount: TextView? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var mFoodAdapter: FoodAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: RestaurantDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RestaurantDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodMenuBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        navController = requireParentFragment().requireParentFragment().findNavController()

        setupRecyclerView()
        setupData()
        setupChipGroup()

        return binding.root
    }

    private fun setupRecyclerView() {
        mFoodAdapter.setListener(this)
        mFoodAdapter.submitList(arrayListOf())
        binding.recyclerFoodMenu.adapter = mFoodAdapter
    }

    private fun setupChipGroup() {
        val chipGroup = binding.chipGroup
        val filterArray = resources.getStringArray(R.array.food_filter)
        var firstChip : Chip? = null
        for (i in filterArray.indices) {
            val chip = layoutInflater.inflate(R.layout.item_chip, chipGroup, false) as Chip
            chip.text = filterArray[i]
            if (i == 0) {
                firstChip = chip
            }
            chipGroup.addView(chip)
        }
        chipGroup.check(firstChip!!.id)
    }

    private fun setupData() {
        val filterArray = resources.getStringArray(R.array.home_filter)
        val restaurantList: ArrayList<Restaurant> = arrayListOf()
        for (filter in filterArray) {
            restaurantList.add(Restaurant(filter, filter, null, false, 0L))
        }
        mFoodAdapter.submitList(restaurantList)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        lytShowCart = requireActivity().findViewById(R.id.lyt_show_cart)
        txtItemsCount = requireActivity().findViewById(R.id.txt_items_count)

        lytShowCart?.setOnClickListener {
            //Toast.makeText(binding.root.context, "lytShowCart", Toast.LENGTH_SHORT).show()
            lytShowCart!!.visibility = View.GONE
            navController.navigate(R.id.fragment_cart)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Restaurant, index: Int) {
        Toast.makeText(binding.root.context, item.title, Toast.LENGTH_SHORT).show()
    }

    override fun onIncClicked(item: Restaurant, index: Int) {
        checkItemsCount()
    }

    override fun onDecClicked(item: Restaurant, index: Int) {
        checkItemsCount()
    }

    override fun onAddClicked(item: Restaurant, index: Int) {
        checkItemsCount()
    }

    @SuppressLint("SetTextI18n")
    private fun checkItemsCount() {
        val itemsCount = mFoodAdapter.getItemsCount()
        Log.d("TAG", "BBB checkItemsCount: $itemsCount")
        if (itemsCount > 0) {
            lytShowCart?.visibility = View.VISIBLE
            txtItemsCount?.text = itemsCount.toString() + " " + getString(R.string.items)
        } else {
            lytShowCart?.visibility = View.GONE
        }
    }
}