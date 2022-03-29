package com.magma.miyyiyawmiyyi.android.presentation.home.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.NotificationsResponse
import com.magma.miyyiyawmiyyi.android.databinding.FragmentNotificationsBinding
import com.magma.miyyiyawmiyyi.android.model.Notification
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import java.util.ArrayList
import javax.inject.Inject

class NotificationsFragment : ProgressBarFragments(), RecyclerItemListener<Notification> {

    private var _binding: FragmentNotificationsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var notificationAdapter: NotificationsAdapter

    private val viewModel: NotificationsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NotificationsViewModel::class.java]
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()
        setObservers()

        return binding.root
    }

    private fun setup() {
        notificationAdapter.setListener(this)
        notificationAdapter.submitList(arrayListOf())
        binding.recyclerNotifications.adapter = notificationAdapter
    }

    private fun setObservers() {
        /*viewModel.purchasesCardsDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<PurchaseCard>> {
                override fun onEventUnhandledContent(t: List<PurchaseCard>) {
                    notificationAdapter.submitList(t)
                    if (t.isNotEmpty()) {
                        binding.progress.visibility = View.GONE
                        binding.txtEmpty.visibility = View.GONE
                    } else {
                        binding.txtEmpty.visibility = View.VISIBLE
                    }
                }
            })
        )*/
        // listen to api result
        viewModel.responseNotifications.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<NotificationsResponse>> {
                override fun onEventUnhandledContent(t: Resource<NotificationsResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.progress.visibility = View.VISIBLE
                            binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.progress.visibility = View.GONE
                            val response = t.response as NotificationsResponse
                            Log.d(TAG, "response: $response")

                            setupData(response.items)
                        }
                        is Resource.DataError -> {
                            binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            binding.progress.visibility = View.GONE
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

    private fun setupData(items: ArrayList<Notification>) {
        notificationAdapter.submitList(items)
        notificationAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.getNotifications(limit = 20, offset = 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Notification, index: Int) {
    }

    companion object {
        private const val TAG = "OurStoreFragment"
    }
}