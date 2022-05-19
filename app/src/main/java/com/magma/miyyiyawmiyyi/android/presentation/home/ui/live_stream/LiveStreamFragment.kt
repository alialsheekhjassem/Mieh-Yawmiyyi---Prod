package com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.InfoResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundStatisticsResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentLiveStreamBinding
import com.magma.miyyiyawmiyyi.android.model.Round
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.DateUtils
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class LiveStreamFragment : ProgressBarFragments() {

    private var _binding: FragmentLiveStreamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var lastDrawUrl: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LiveStreamViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LiveStreamViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLiveStreamBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setUpObservers()

        return binding.root
    }

    private fun setUp() {
        val info = ContactManager.getCurrentInfo()
        info?.let { inf ->
            lastDrawUrl = inf.lastCompletedRoundUrl ?: ""
            if (inf.currentGrandPrize != null) {
                val date = inf.currentGrandPrize.drawResultAt
                viewModel.onStartGoldenCountDown(DateUtils.formatDateTimeToLong(date))
                binding.lytGolden.visibility = View.VISIBLE
            } else {
                binding.lytGolden.visibility = View.GONE
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getRoundStatistics(true)
        }
    }

    private fun setUpObservers() {

        viewModel.actions.observe(
            viewLifecycleOwner, EventObserver(
                object : EventObserver.EventUnhandledContent<LiveStreamActions> {
                    override fun onEventUnhandledContent(t: LiveStreamActions) {
                        when (t) {
                            LiveStreamActions.TASKS_CLICKED -> {
                                Log.d(TAG, "TRT onEventUnhandledContent: TASKS_CLICKED")
                                findNavController().navigate(
                                    LiveStreamFragmentDirections.actionLiveStreamToTasks()
                                )
                            }
                            LiveStreamActions.SHOW_LAST_DRAW_CLICKED -> {
                                lastDrawUrl?.let { openWebUrl(it) }
                            }
                        }
                    }
                })
        )

        //observe 100$ round
        viewModel.responseCountDown.observe(
            viewLifecycleOwner
        ) {
            if (it <= 0) {
                setViewsTime(Const.INIT_COUNT_DOWN)
                binding.btnWatchNow.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.light_blue_2)
                binding.btnWatchNow.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnWatchNow.isEnabled = true
            } else {
                DateUtils.formatLongToCountDown(it)?.let { it1 -> setViewsTime(it1) }
                binding.btnWatchNow.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey_13)
                binding.btnWatchNow.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.grey_14
                    )
                )
                binding.btnWatchNow.isEnabled = false
            }
        }
        //observe Golden Lira round
        viewModel.responseGoldenCountDown.observe(
            viewLifecycleOwner
        ) {
            if (it <= 0) {
                setGoldenViewsTime(Const.INIT_COUNT_DOWN)
                binding.btnWatchNowGolden.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue_2
                    )
                )
                binding.btnWatchNowGolden.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.btnWatchNowGolden.isEnabled = true
            } else {
                DateUtils.formatLongToCountDown(it)?.let { it1 -> setGoldenViewsTime(it1) }
                binding.btnWatchNowGolden.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.grey_13)
                binding.btnWatchNowGolden.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_13
                    )
                )
                binding.btnWatchNowGolden.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.grey_14
                    )
                )
                binding.btnWatchNowGolden.isEnabled = false
            }
        }
        // listen to api result
        /*viewModel.response.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<RoundsResponse>> {
                override fun onEventUnhandledContent(t: Resource<RoundsResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            //binding.progress.visibility = View.VISIBLE
                            //binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            //binding.progress.visibility = View.GONE
                            val response = t.response as RoundsResponse
                            Log.d(TAG, "response: $response")

                            //viewModel.deleteAndSaveTasks(response)
                            setupData(response.items)
                        }
                        is Resource.DataError -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError ${response.status}")
                            showErrorToast(response.toString())
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
        )*/

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

                            setupData(ContactManager.getCurrentInfo()?.currentRound)
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
        // listen to api result
        viewModel.responseStatistics.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<RoundStatisticsResponse>> {
                override fun onEventUnhandledContent(t: Resource<RoundStatisticsResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.swipeRefresh.isRefreshing = true
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.swipeRefresh.isRefreshing = false
                            val response = t.response as RoundStatisticsResponse
                            Log.d(TAG, "response: $response")
                            setTicketsProgress(response)
                        }
                        is Resource.DataError -> {
                            // usually this happening when there is server error
                            binding.swipeRefresh.isRefreshing = false
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            if (response.status == 404)
                                binding.txtEmpty.visibility = View.VISIBLE
                            else showErrorToast(response.toString())
                        }
                        is Resource.Exception -> {
                            binding.swipeRefresh.isRefreshing = false
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

    private fun setupData(activeRound: Round?) {
        activeRound?.type?.let {
            setStreamView(it)
            if (it == Const.TYPE_ROUND_START_DRAW) {
                val date = activeRound.drawResultAt
                viewModel.onStartCountDown(DateUtils.formatDateTimeToLong(date))
            } /*else if (it == Const.TYPE_ROUND_TICKETS_DRAW) {
                        activeRound.fixedTicketsDraw?.maxTickets?.let { maxTickets ->
                            val percentage = 2 * 100 / maxTickets
                            binding.txtPercentage.text = percentage.toString()
                            binding.imgProgress.progress = percentage
                        }
                    }*/
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setTicketsProgress(response: RoundStatisticsResponse) {
        val maxTickets = response.maxTickets ?: 0
        val availableTickets = response.availableTickets ?: 0
        val issuedTickets = response.issuedTickets ?: 0
        if (maxTickets != 0) {
            val percentage = issuedTickets * 100 / maxTickets
            binding.txtPercentage.text = issuedTickets.toString()
            binding.txtPercent.text = "/$maxTickets"
            binding.imgProgress.progress = percentage
        } else {
            binding.txtPercentage.text = issuedTickets.toString()
            binding.txtPercent.text = "/$maxTickets"
            binding.imgProgress.progress = 0
        }

    }

    private fun setStreamView(type: String) {
        if (type == Const.TYPE_ROUND_START_DRAW) {
            binding.groupTime.visibility = View.VISIBLE
            binding.cardTickets.visibility = View.GONE
        } else if (type == Const.TYPE_ROUND_TICKETS_DRAW) {
            binding.groupTime.visibility = View.GONE
            binding.cardTickets.visibility = View.VISIBLE
        }
    }

    private fun setViewsTime(time: String) {
        val timeArray = time.split(":")
        if (timeArray.size == 4) {
            //Day
            binding.txtDays11.text = timeArray[0][0].toString()
            if (timeArray[0].length == 1) binding.txtDays12.text = "0"
            else binding.txtDays12.text = timeArray[0][1].toString()

            //Hour
            binding.txtHours11.text = timeArray[1][0].toString()
            if (timeArray[1].length == 1) binding.txtHours12.text = "0"
            else binding.txtHours12.text = timeArray[1][1].toString()

            //Minute
            binding.txtMinutes11.text = timeArray[2][0].toString()
            if (timeArray[2].length == 1) binding.txtMinutes12.text = "0"
            else binding.txtMinutes12.text = timeArray[2][1].toString()

            //Second
            binding.txtSeconds11.text = timeArray[3][0].toString()
            if (timeArray[3].length == 1) binding.txtSeconds12.text = "0"
            else binding.txtSeconds12.text = timeArray[3][1].toString()
        }
    }

    private fun setGoldenViewsTime(time: String) {
        val timeArray = time.split(":")
        if (timeArray.size == 4) {
            //Day
            binding.txtDays1.text = timeArray[0][0].toString()
            if (timeArray[0].length == 1) binding.txtDays2.text = "0"
            else binding.txtDays2.text = timeArray[0][1].toString()

            //Hour
            binding.txtHours1.text = timeArray[1][0].toString()
            if (timeArray[1].length == 1) binding.txtHours2.text = "0"
            else binding.txtHours2.text = timeArray[1][1].toString()

            //Minute
            binding.txtMinutes1.text = timeArray[2][0].toString()
            if (timeArray[2].length == 1) binding.txtMinutes2.text = "0"
            else binding.txtMinutes2.text = timeArray[2][1].toString()

            //Second
            binding.txtSeconds1.text = timeArray[3][0].toString()
            if (timeArray[3].length == 1) binding.txtSeconds2.text = "0"
            else binding.txtSeconds2.text = timeArray[3][1].toString()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.getInfo()
        viewModel.getRoundStatistics(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LiveStreamFragment"
    }
}