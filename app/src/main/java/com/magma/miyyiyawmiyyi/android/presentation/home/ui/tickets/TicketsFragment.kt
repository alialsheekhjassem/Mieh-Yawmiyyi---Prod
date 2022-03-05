package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentTicketsBinding
import com.magma.miyyiyawmiyyi.android.model.Winner
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import javax.inject.Inject

class TicketsFragment : Fragment(), RecyclerItemListener<Winner> {

    private var _binding: FragmentTicketsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var ticketsAdapter: TicketsAdapter

    private val viewModel: TicketsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TicketsViewModel::class.java]
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()

        return binding.root
    }

    private fun setup() {
        ticketsAdapter.setListener(this)
        ticketsAdapter.submitList(arrayListOf())
        binding.recyclerTickets.adapter = ticketsAdapter

        setupData()
    }

    private fun setupData() {
        val winnerList: ArrayList<Winner> = arrayListOf()
        winnerList.add(
            Winner(
                0, 0,
            Const.TYPE_100DOLLAR,"Ali Jassem", "12562158")
        )
        winnerList.add(
            Winner(
                0, 0,
            Const.TYPE_GOLDEN_LIRA,"Ali Jassem", "12562158")
        )
        ticketsAdapter.submitList(winnerList)
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
}