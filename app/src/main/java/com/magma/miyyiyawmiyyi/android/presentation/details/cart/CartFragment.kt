package com.magma.miyyiyawmiyyi.android.presentation.details.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentCartBinding
import com.magma.miyyiyawmiyyi.android.model.Restaurant
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemCartListener
import javax.inject.Inject

class CartFragment : Fragment(), RecyclerItemCartListener<Restaurant> {

    private var _binding: FragmentCartBinding? = null
    private lateinit var navController: NavController
    private var btnClose: ImageButton? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var mCartAdapter: CartAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CartViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        navController = requireParentFragment().findNavController()

        setUp()
        setupData()

        return binding.root
    }

    private fun setUp() {
        binding.btnContinue.setOnClickListener {
            navController.navigate(R.id.fragment_address)
        }
        mCartAdapter.setListener(this)
        mCartAdapter.submitList(arrayListOf())
        binding.recyclerItems.adapter = mCartAdapter
    }

    private fun setupData() {
        val filterArray = resources.getStringArray(R.array.home_filter)
        val restaurantList: ArrayList<Restaurant> = arrayListOf()
        for (filter in filterArray) {
            restaurantList.add(Restaurant(filter, filter, null, false, 1L))
        }
        mCartAdapter.submitList(restaurantList)
    }

    override fun onStart() {
        super.onStart()
        btnClose = requireActivity().findViewById(R.id.btn_close)
        btnClose?.visibility = View.VISIBLE

        btnClose?.setOnClickListener {
            navController.popBackStack()
            btnClose?.visibility = View.GONE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Restaurant, index: Int) {
        Toast.makeText(binding.root.context, item.title, Toast.LENGTH_SHORT).show()
    }

    override fun onIncClicked(item: Restaurant, index: Int) {
        //Toast.makeText(binding.root.context, item.title + " Inc", Toast.LENGTH_SHORT).show()
    }

    override fun onDecClicked(item: Restaurant, index: Int) {
        //Toast.makeText(binding.root.context, item.title + " Dec", Toast.LENGTH_SHORT).show()
    }
}