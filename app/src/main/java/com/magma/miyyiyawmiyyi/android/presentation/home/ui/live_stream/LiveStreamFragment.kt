package com.magma.miyyiyawmiyyi.android.presentation.home.ui.live_stream

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.RoundsResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentLiveStreamBinding
import com.magma.miyyiyawmiyyi.android.model.Round
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class LiveStreamFragment : ProgressBarFragments() {

    private var _binding: FragmentLiveStreamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        setUpObservers()

        return binding.root
    }

    private fun setupData(items: ArrayList<Round>) {
        if (items.isNotEmpty()) {
            val activeRounds = items.filter { round -> round.status.equals("active") }

            if (activeRounds.isNotEmpty()) {
                val activeRound = activeRounds.first()
                activeRound.type?.let {
                    setStreamView(it)
                    if (it == Const.TYPE_ROUND_START_DRAW) {
                        val percentage = activeRound.fixedStartDraw?.minutesBeforeClose
                        binding.txtMinutes1.text = percentage.toString()
                    } else if (it == Const.TYPE_ROUND_TICKETS_DRAW) {
                        activeRound.fixedTicketsDraw?.maxTickets?.let { maxTickets ->
                            val percentage = 2 * 100 / maxTickets
                            binding.txtPercentage.text = percentage.toString()
                            binding.imgProgress.progress = percentage
                        }
                    }
                }
            }

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

    private fun setUpObservers() {
        // listen to api result
        viewModel.response.observe(
            this,
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

        viewModel.getRounds(limit = 20, offset = 0, Const.STATUS_ACTIVE, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LiveStreamFragment"
    }
}