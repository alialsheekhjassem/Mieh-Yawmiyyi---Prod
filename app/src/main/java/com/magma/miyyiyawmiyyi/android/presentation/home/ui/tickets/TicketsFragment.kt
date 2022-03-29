package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tickets

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TicketsResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentTicketsBinding
import com.magma.miyyiyawmiyyi.android.model.Ticket
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class TicketsFragment : ProgressBarFragments(), RecyclerItemListener<Ticket> {

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
        setUpObservers()

        return binding.root
    }

    private fun setup() {
        ticketsAdapter.setListener(this)
        ticketsAdapter.submitList(arrayListOf())
        binding.recyclerTickets.adapter = ticketsAdapter

        viewModel.loadAllTickets()

        val dRoundNumber = ContactManager.getCurrentInfo()?.activeRound?.let {
            it.number?.let { num ->
                String.format(getString(R.string.draw_number_1), num)
            } ?: String.format(getString(R.string.draw_number_1), 0)
        } ?: String.format(getString(R.string.draw_number_1), 0)
        binding.txt100DDrawNumber.text = dRoundNumber

        val goldenRoundNumber = ContactManager.getCurrentInfo()?.activeGrandPrize?.let {
            it.number?.let { num ->
                String.format(getString(R.string.draw_number_1), num)
            } ?: String.format(getString(R.string.draw_number_1), 0)
        } ?: String.format(getString(R.string.draw_number_1), 0)
        binding.txtDrawNumber.text = goldenRoundNumber


        binding.btnGetItNow.setOnClickListener {
            findNavController().navigate(TicketsFragmentDirections.actionTicketsToTasks())
        }
    }

    private fun setUpObservers() {
        viewModel.ticketsDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<Ticket>> {
                override fun onEventUnhandledContent(t: List<Ticket>) {
                    val normalList = t.filter { ticket -> ticket.round != null }
                    val goldenList = t.filter { ticket -> ticket.grandPrize != null }
                    ticketsAdapter.submitList(normalList)
                    if (goldenList.isNotEmpty()){
                        binding.txtTicketNum.text = goldenList.first().number
                    }
                    if (t.isNotEmpty()) {
                        //binding.progress.visibility = View.GONE
                        //binding.txtEmpty.visibility = View.GONE
                    } else {
                        //binding.txtEmpty.visibility = View.VISIBLE
                    }
                }
            })
        )
        // listen to api result
        viewModel.response.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<TicketsResponse>> {
                override fun onEventUnhandledContent(t: Resource<TicketsResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            //binding.progress.visibility = View.VISIBLE
                            //binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            //binding.progress.visibility = View.GONE
                            val response = t.response as TicketsResponse
                            Log.d(TAG, "response: $response")

                            viewModel.deleteAndSaveTickets(response.items)
                        }
                        is Resource.DataError -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            showErrorToast(response.toString())
                        }
                    }
                }
            })
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.getTickets(limit = 20, offset = 0, null, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Ticket, index: Int) {

    }

    companion object {
        private const val TAG = "TicketsFragment"
    }
}