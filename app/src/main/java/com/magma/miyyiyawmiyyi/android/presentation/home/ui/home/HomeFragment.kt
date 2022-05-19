package com.magma.miyyiyawmiyyi.android.presentation.home.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentHomeBinding
import com.magma.miyyiyawmiyyi.android.model.Round
import com.magma.miyyiyawmiyyi.android.model.Winner
import com.magma.miyyiyawmiyyi.android.model.WinnerObj
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.Presets
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import nl.dionsegijn.konfetti.xml.KonfettiView
import javax.inject.Inject

class HomeFragment : ProgressBarFragments(), RecyclerItemListener<Winner> {

    lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var winnersAdapter: WinnersAdapter

    private lateinit var viewKonfetti: KonfettiView

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setUpObservers()

        return binding.root
    }

    private fun setUp() {
        arguments?.getBoolean("IS_WINNER")?.let {
            if (it) {
                viewKonfetti = binding.konfettiView
                viewKonfetti.start(Presets.rain())
            }
        }

        if (ContactManager.getIsRefreshInfo()) {
            viewModel.getInfo()
        }

        winnersAdapter.setListener(this)
        winnersAdapter.submitList(arrayListOf())
        binding.recyclerWinners.adapter = winnersAdapter

        setHasOptionsMenu(true)

        viewModel.loadAllRounds()

        binding.cardPoints.txtView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToInvitations())
        }
        binding.cardTickets.txtView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToTickets())
        }

        binding.lytSwipeRefresh.setOnRefreshListener {
            viewModel.getInfo()
        }
    }

    private fun setupData(roundDb: Round?) {
        var round = roundDb
        val info = ContactManager.getCurrentInfo()
        val winnerList: ArrayList<Winner> = arrayListOf()
        var roundWinner: WinnerObj? = null
        var grandPrizeWinner: WinnerObj? = null

        if (round == null) {
            round = info?.currentRound
        }

        Log.d(TAG, "QQQ setupData: info $info")

        info?.let { inf ->
            binding.cardPoints.value = "${inf.userPoints ?: 0} ${getString(R.string.points)}"
            binding.cardTickets.value =
                round?.config?.let { config ->
                    config.maxTicketsPerContestant?.let { maxTickets ->
                        inf.currentRoundTickets?.let { amount ->
                            "$amount/$maxTickets ${
                                getString(R.string.ticket)
                            }"
                        } ?: "0/0 ${getString(R.string.ticket)}"
                    } ?: "0/0 ${getString(R.string.ticket)}"
                } ?: "0/0 ${getString(R.string.ticket)}"

            Log.d(TAG, "QQQ setupData: settings ${inf.settings}")
            Log.d(TAG, "QQQ setupData: roundWinner ${inf.roundWinner?.number}")
            roundWinner = inf.roundWinner
            grandPrizeWinner = inf.grandPrizeWinner
        }

        winnerList.add(
            Winner(
                R.drawable.trophy,
                R.drawable.ic_metro_trophy,
                Const.TYPE_100DOLLAR,
                roundWinner?.user?.name ?: getString(R.string.no_winner),
                roundWinner?.number ?: "------"
            )
        )
        winnerList.add(
            Winner(
                R.drawable.golden_lira,
                R.drawable.ic_awesome_ticket,
                Const.TYPE_GOLDEN_LIRA,
                grandPrizeWinner?.user?.name ?: getString(R.string.no_winner),
                grandPrizeWinner?.number ?: "------"
            )
        )
        winnersAdapter.submitList(winnerList)
    }

    private fun setUpObservers() {
        viewModel.roundsDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<Round>> {
                override fun onEventUnhandledContent(t: List<Round>) {
                    if (t.isNotEmpty())
                        setupData(t.first())
                    else setupData(null)
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
                            binding.lytSwipeRefresh.isRefreshing = true
                        }
                        is Resource.Success -> {
                            binding.lytSwipeRefresh.isRefreshing = false
                            // response is ok get the data and display it in the list
                            val response = t.response as InfoResponse
                            Log.d(TAG, "response: $response")
                            ContactManager.setInfo(response)
                            ContactManager.setIsRefreshInfo(false)

                            setupData(ContactManager.getCurrentInfo()?.currentRound)
                        }
                        is Resource.DataError -> {
                            binding.lytSwipeRefresh.isRefreshing = false
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                        }
                        is Resource.Exception -> {
                            binding.lytSwipeRefresh.isRefreshing = false
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
    }

    override fun onResume() {
        super.onResume()

        //Temporary Solution For Refresh Data
        viewModel.getInfo()
    }

    override fun onItemClicked(item: Winner, index: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_notifications -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeToNotifications())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}