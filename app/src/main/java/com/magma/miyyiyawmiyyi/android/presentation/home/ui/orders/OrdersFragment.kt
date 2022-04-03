package com.magma.miyyiyawmiyyi.android.presentation.home.ui.orders

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GiftStorePurchasesResponse
import com.magma.miyyiyawmiyyi.android.databinding.DialogPurchaseCardCodeBinding
import com.magma.miyyiyawmiyyi.android.databinding.FragmentOrdersBinding
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store.PurchaseCardsAdapter
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemCardListener
import javax.inject.Inject

class OrdersFragment : ProgressBarFragments(), RecyclerItemCardListener<PurchaseCard> {

    private var _binding: FragmentOrdersBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var purchaseCardsAdapter: PurchaseCardsAdapter

    private var selectedCard: PurchaseCard? = null

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
        setObservers()

        return binding.root
    }

    private fun setup() {
        purchaseCardsAdapter.setListener(this)
        purchaseCardsAdapter.submitList(arrayListOf())
        binding.recyclerOrders.adapter = purchaseCardsAdapter
    }

    private fun setObservers() {
        viewModel.purchasesCardsDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<PurchaseCard>> {
                override fun onEventUnhandledContent(t: List<PurchaseCard>) {
                    purchaseCardsAdapter.submitList(t)
                    if (t.isNotEmpty()) {
                        binding.progress.visibility = View.GONE
                        binding.txtEmpty.visibility = View.GONE
                    } else {
                        binding.txtEmpty.visibility = View.VISIBLE
                    }
                }
            })
        )
        // listen to api result
        viewModel.responsePurchases.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<GiftStorePurchasesResponse>> {
                override fun onEventUnhandledContent(t: Resource<GiftStorePurchasesResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.progress.visibility = View.VISIBLE
                            binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.progress.visibility = View.GONE
                            val response = t.response as GiftStorePurchasesResponse
                            Log.d(TAG, "response: $response")

                            viewModel.deleteAndSavePurchaseCards(response)
                            //setupData(response.items)
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

        // listen to api result
        viewModel.responseCode.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Any?>> {
                override fun onEventUnhandledContent(t: Resource<Any?>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response
                            Log.d(TAG, "response: $response")

                            selectedCard?.let { showGetCodeDialog(it) }
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
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

        viewModel.getPurchaseCards(limit = 20, offset = 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemCardClicked(item: PurchaseCard, index: Int) {
        if (item.status == Const.STATUS_COMPLETED) {
            selectedCard = item
            viewModel.getGiftCode(item._id)
        } else {
            showToast(getString(R.string.not_completed_card))
        }
    }

    private fun showGetCodeDialog(item: PurchaseCard) {
        val builder = AlertDialog.Builder(requireActivity())
        val dialogBinding: DialogPurchaseCardCodeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireActivity()), R.layout.dialog_purchase_card_code, null, false
        )
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBinding.item = item
        dialogBinding.imgCopy.setOnClickListener {
            item.code?.let { it1 -> copyText(it1) }
        }
        dialogBinding.btnClose.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    companion object {
        private const val TAG = "OurStoreFragment"
    }
}