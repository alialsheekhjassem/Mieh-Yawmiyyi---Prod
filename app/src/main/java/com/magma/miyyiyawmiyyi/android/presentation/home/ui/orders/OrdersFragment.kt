package com.magma.miyyiyawmiyyi.android.presentation.home.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.databinding.FragmentOrdersBinding
import com.magma.miyyiyawmiyyi.android.model.Ticket
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import javax.inject.Inject

class OrdersFragment : Fragment(), RecyclerItemListener<Ticket> {

    private var _binding: FragmentOrdersBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var ticketsAdapter: OrdersAdapter

    private val viewModel: OrdersViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OrdersViewModel::class.java]
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()

        return binding.root
    }

    private fun setup() {
        ticketsAdapter.setListener(this)
        ticketsAdapter.submitList(arrayListOf())
        binding.recyclerOrders.adapter = ticketsAdapter

        setupData()
    }

    private fun setupData() {
        val ticketList: ArrayList<Ticket> = arrayListOf()
        ticketList.add(
            Ticket(
                "0", "0",
            Const.TYPE_100DOLLAR,"Ali Jassem", "","12562158")
        )
        ticketList.add(
            Ticket(
                "0", "0",
            Const.TYPE_GOLDEN_LIRA,"Ali Jassem", "", "12562158")
        )
        ticketsAdapter.submitList(ticketList)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Ticket, index: Int) {

    }
}