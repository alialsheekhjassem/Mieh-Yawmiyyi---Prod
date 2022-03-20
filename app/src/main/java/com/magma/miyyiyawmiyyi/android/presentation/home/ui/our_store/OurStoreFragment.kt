package com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store

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
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CreatePurchaseResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GiftStorePurchasesResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GiftStoreResponse
import com.magma.miyyiyawmiyyi.android.databinding.DialogGiftCardBinding
import com.magma.miyyiyawmiyyi.android.databinding.DialogPurchaseCardBinding
import com.magma.miyyiyawmiyyi.android.databinding.FragmentOurStoreBinding
import com.magma.miyyiyawmiyyi.android.model.GiftCard
import com.magma.miyyiyawmiyyi.android.model.PurchaseCard
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemCardListener
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import javax.inject.Inject

class OurStoreFragment : ProgressBarFragments(), RecyclerItemListener<GiftCard>,
    RecyclerItemCardListener<PurchaseCard> {

    lateinit var binding: FragmentOurStoreBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var giftCardsAdapter: GiftCardsAdapter

    @Inject
    lateinit var purchaseCardsAdapter: PurchaseCardsAdapter

    private val viewModel: OurStoreViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OurStoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOurStoreBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setObservers()

        return binding.root
    }

    private fun setUp() {
        giftCardsAdapter.setListener(this)
        giftCardsAdapter.submitList(arrayListOf())
        binding.recyclerGoogleCards.adapter = giftCardsAdapter

        purchaseCardsAdapter.setListener(this)
        purchaseCardsAdapter.submitList(arrayListOf())
        binding.recycler100YawCards.adapter = purchaseCardsAdapter

        viewModel.loadAllGiftStoreCards()
        viewModel.loadAllPurchaseCards()
    }

    private fun setObservers() {
        viewModel.giftStoreCardsDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<GiftCard>> {
                override fun onEventUnhandledContent(t: List<GiftCard>) {
                    giftCardsAdapter.submitList(t)
                    if (t.isNotEmpty()) {
                        binding.progress.visibility = View.GONE
                        binding.txtEmpty.visibility = View.GONE
                    } else {
                        binding.txtEmpty.visibility = View.VISIBLE
                    }
                }
            })
        )
        viewModel.purchasesCardsDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :


                EventObserver.EventUnhandledContent<List<PurchaseCard>> {
                override fun onEventUnhandledContent(t: List<PurchaseCard>) {
                    purchaseCardsAdapter.submitList(t)
                    if (t.isNotEmpty()) {
                        binding.progress.visibility = View.GONE
                        binding.txtEmptyPurchases.visibility = View.GONE
                    } else {
                        binding.txtEmptyPurchases.visibility = View.VISIBLE
                    }
                }
            })
        )
        // listen to api result
        viewModel.response.observe(
            this,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<GiftStoreResponse>> {
                override fun onEventUnhandledContent(t: Resource<GiftStoreResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.progress.visibility = View.VISIBLE
                            binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.progress.visibility = View.GONE
                            val response = t.response as GiftStoreResponse
                            Log.d(TAG, "response: $response")

                            viewModel.deleteAndSaveGiftStoreCards(response)
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
        viewModel.responsePurchases.observe(
            this,
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
        viewModel.responseCreatePurchase.observe(
            requireActivity(),
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<CreatePurchaseResponse>> {
                override fun onEventUnhandledContent(t: Resource<CreatePurchaseResponse>) {
                    hideKeyboard()
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response as CreatePurchaseResponse
                            Log.d(TAG, "createPurchaseResponse: $response")
                            showSuccessToast(getString(R.string.success))
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "createPurchaseResponse: DataError $response")
                            showToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "createPurchaseResponse: Exception $response")
                            showToast(response.toString())
                        }
                    }
                }
            })
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.getGiftStore(limit = 20, offset = 0)
        viewModel.getPurchaseCards(limit = 20, offset = 0)
    }

    override fun onItemClicked(item: GiftCard, index: Int) {
        showGiftDialog(item)
    }

    private fun showGiftDialog(item: GiftCard) {
        val builder = AlertDialog.Builder(requireActivity())
        val dialogBinding: DialogGiftCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireActivity()), R.layout.dialog_gift_card, null, false
        )
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBinding.item = item
        dialogBinding.btnContinue.setOnClickListener {
            if (item._id.isNotEmpty()){
                viewModel.doServerCreatePurchase(item._id)
            }
            alertDialog.dismiss()
        }
        dialogBinding.btnClose.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    override fun onItemCardClicked(item: PurchaseCard, index: Int) {
        showPurchaseDialog(item)
    }

    private fun showPurchaseDialog(item: PurchaseCard) {
        val builder = AlertDialog.Builder(requireActivity())
        val dialogBinding: DialogPurchaseCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireActivity()), R.layout.dialog_purchase_card, null, false
        )
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBinding.item = item
        dialogBinding.btnContinue.setOnClickListener {
            //showSuccessToast(item._id)
            //Do your logic
            if (item._id.isNotEmpty()){
                viewModel.doServerCreatePurchase(item._id)
            }
            alertDialog.dismiss()
        }
        dialogBinding.btnClose.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    companion object {
        private const val TAG = "OurStoreFragment"
    }
}