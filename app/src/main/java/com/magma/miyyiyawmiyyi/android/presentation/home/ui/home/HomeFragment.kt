package com.magma.miyyiyawmiyyi.android.presentation.home.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentHomeBinding
import com.magma.miyyiyawmiyyi.android.model.Round
import com.magma.miyyiyawmiyyi.android.model.Winner
import com.magma.miyyiyawmiyyi.android.model.WinnerObj
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class HomeFragment : ProgressBarFragments(), RecyclerItemListener<Winner> {

    lateinit var binding: FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()

        return binding.root
    }

    private fun setup() {
        winnersAdapter.setListener(this)
        winnersAdapter.submitList(arrayListOf())
        binding.recyclerWinners.adapter = winnersAdapter

        setUp()
        setUpObservers()
    }

    private fun setUp() {
        viewModel.loadAllTickets()

        binding.cardPoints.txtView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToInvitations())
        }
        binding.cardTickets.txtView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToTickets())
        }
    }

    private fun setupData(roundDb: Round?) {
        var round = roundDb
        val info = ContactManager.getCurrentInfo()
        val winnerList: ArrayList<Winner> = arrayListOf()
        var roundWinner: WinnerObj? = null
        var grandPrizeWinner: WinnerObj? = null

        if (round == null) {
            round = info?.activeRound
        }

        Log.d(TAG, "QQQ setupData: info $info")

        info?.let { inf ->
            binding.cardPoints.value = "${inf.userPoints ?: 0} ${getString(R.string.points)}"
            binding.cardTickets.value =
                round?.config?.let { config ->
                    config.tasksPerTicket?.let { tPTicket ->
                        config.maxTicketsPerContestant?.let { max ->
                            "$tPTicket/$max ${
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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onItemClicked(item: Winner, index: Int) {

    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}