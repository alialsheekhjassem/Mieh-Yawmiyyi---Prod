package com.magma.miyyiyawmiyyi.android.presentation.home.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.R
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentHomeBinding
import com.magma.miyyiyawmiyyi.android.model.Winner
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import javax.inject.Inject

class HomeFragment : ProgressBarFragments(), RecyclerItemListener<Winner> {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapterSliderView: AdapterSliderView

    @Inject
    lateinit var winnersAdapter: WinnersAdapter

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()

        return binding.root
    }

    private fun setup() {
        winnersAdapter.setListener(this)
        winnersAdapter.submitList(arrayListOf())
        binding.recyclerWinners.adapter = winnersAdapter

        setupData()
        setUpObservers()
    }

    private fun setupData() {
        val winnerList: ArrayList<Winner> = arrayListOf()
        winnerList.add(Winner(R.drawable.trophy, R.drawable.ic_metro_trophy,
            Const.TYPE_100DOLLAR,"Ali Jassem", "12562158"))
        winnerList.add(Winner(R.drawable.golden_lira, R.drawable.ic_awesome_ticket,
            Const.TYPE_GOLDEN_LIRA,"Ali Jassem", "12562158"))
        winnersAdapter.submitList(winnerList)
    }

    private fun setUpObservers() {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Winner, index: Int) {

    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}