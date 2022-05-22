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
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TicketsResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentTicketsBinding
import com.magma.miyyiyawmiyyi.android.model.Ticket
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.Presets
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemTicketListener
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import nl.dionsegijn.konfetti.xml.KonfettiView
import javax.inject.Inject

class TicketsFragment : ProgressBarFragments(), RecyclerItemTicketListener<Ticket> {

    private var _binding: FragmentTicketsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var ticketsAdapter: TicketsAdapter

    private lateinit var viewKonfetti: KonfettiView

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

        setViews()
        setup()
        setUpObservers()

        return binding.root
    }

    private fun setViews() {

        arguments?.getBoolean("IS_WINNER")?.let {
            if (it) {
                viewKonfetti = binding.konfettiView
                viewKonfetti.start(Presets.rain())
            }
        }

        ticketsAdapter.setListener(this)
        ticketsAdapter.submitList(arrayListOf())
        binding.recyclerTickets.adapter = ticketsAdapter
    }

    private fun setup() {
        ContactManager.getCurrentInfo()?.let {
            val dRoundNumber = when (it.currentRound) {
                null -> {
                    String.format(getString(R.string.draw_number_1), 0)
                }
                else -> {
                    Log.d(TAG, "setup: ${it.currentRound.number}")
                    it.currentRound.number?.let { num ->
                        String.format(getString(R.string.draw_number_1), num)
                    } ?: String.format(getString(R.string.draw_number_1), 0)
                }
            }
            Log.d(TAG, "setup: dRoundNumber $dRoundNumber")
            binding.txt100DDrawNumber.text = dRoundNumber

            val goldenRoundNumber = when (it.currentGrandPrize) {
                null -> {
                    String.format(getString(R.string.draw_number_1), 0)
                }
                else -> {
                    it.currentGrandPrize.number?.let { num ->
                        String.format(getString(R.string.draw_number_1), num)
                    } ?: String.format(getString(R.string.draw_number_1), 0)
                }
            }
            binding.txtDrawNumber.text = goldenRoundNumber

            val round = if (!it.currentRound?._id.isNullOrEmpty()) {
                    it.currentRound?._id
                } else {
                    null
                }
            val roundGrand = if (!it.currentGrandPrize?._id.isNullOrEmpty()) {
                    it.currentGrandPrize?._id
                } else {
                    null
                }
            Log.d(TAG, "YYY onAttach: roundGrand $roundGrand")
            round?.let {
                viewModel.getTickets(
                    limit = 20,
                    offset = 0,
                    grandPrize = null,
                    round = round,
                    populate = null
                )
            }
            roundGrand?.let {
                viewModel.getTickets(
                    limit = 20,
                    offset = 0,
                    grandPrize = roundGrand,
                    round = null,
                    populate = null
                )
            }
        }

        /*val goldenTicketNum = ContactManager.getCurrentInfo()?.grandPrizeWinner?.number ?: ""
        if (goldenTicketNum.isNotEmpty()) {
            binding.txtTicketNum.text = goldenTicketNum
            binding.lytGoldenPoll.visibility = View.VISIBLE
        } else {
            binding.lytGoldenPoll.visibility = View.GONE
        }*/

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
                    if (goldenList.isNotEmpty()) {
                        binding.txtTicketNum.text = goldenList.first().number
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
                            binding.progress.visibility = View.VISIBLE
                            //binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.progress.visibility = View.GONE
                            // response is ok get the data and display it in the list
                            //binding.progress.visibility = View.GONE
                            val response = t.response as TicketsResponse
                            Log.d(TAG, "response: $response")

                            val totalTickets = arrayListOf<Ticket>()
                            val normalList =
                                response.items.filter { ticket -> ticket.round != null }
                            val goldenList =
                                response.items.filter { ticket -> ticket.grandPrize != null }
                            //if (normalList.isNotEmpty()) {
                            val getItNowCards = arrayListOf<Ticket>()
                            ContactManager.getCurrentInfo().let {
                                if (it?.currentRound != null) {
                                    if ((it.currentRoundTickets ?: 0)
                                        < (it.currentRound.config?.maxTicketsPerContestant ?: 0)
                                    ) {
                                        val getItNowCount =
                                            (it.currentRound.config?.maxTicketsPerContestant?.minus(
                                                (it.currentRoundTickets ?: 0)
                                            )) ?: 0
                                        for (i in 1..getItNowCount) {
                                            getItNowCards.add(
                                                Ticket(
                                                    _id = Const.TYPE_GET_IT_NOW,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            if (normalList.isNotEmpty()) {
                                totalTickets.addAll(normalList)
                            }
                            if (getItNowCards.isNotEmpty()) {
                                totalTickets.addAll(getItNowCards)
                            }
                            ticketsAdapter.submitList(totalTickets)
                            //}
                            if (goldenList.isNotEmpty()) {
                                binding.txtTicketNum.text = goldenList.first().number
                                binding.lytGoldenPoll.visibility = View.VISIBLE
                            } else {
                                binding.lytGoldenPoll.visibility = View.GONE
                            }

                            //viewModel.deleteAndSaveTickets(response.items)
                        }
                        is Resource.DataError -> {
                            binding.progress.visibility = View.GONE
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            binding.progress.visibility = View.GONE
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

        // listen to api result
        viewModel.infoResponse.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<InfoResponse>> {
                override fun onEventUnhandledContent(t: Resource<InfoResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            val response = t.response as InfoResponse
                            Log.d(TAG, "response: $response")
                            ContactManager.setInfo(response)
                            ContactManager.setIsRefreshInfo(false)
                            setup()
                        }
                        is Resource.DataError -> {
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                        }
                        is Resource.Exception -> {
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                        }
                    }
                }
            })
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        if (ContactManager.getIsRefreshInfo())
            viewModel.getInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Ticket, index: Int) {

    }

    override fun onGetItNowClicked(item: Ticket, index: Int) {
        findNavController().navigate(
            TicketsFragmentDirections.actionTicketsToTasks()
        )
    }

    companion object {
        private const val TAG = "TicketsFragment"
    }
}