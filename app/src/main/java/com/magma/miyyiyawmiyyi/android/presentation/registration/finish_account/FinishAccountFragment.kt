package com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CountriesResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import javax.inject.Inject
import com.magma.miyyiyawmiyyi.android.databinding.FragmentFinishAccountBinding
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.model.Country
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.*
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import java.util.*

class FinishAccountFragment : ProgressBarFragments() {

    lateinit var binding: FragmentFinishAccountBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var listYear: Array<String> = arrayOf()
    var countryList: Array<Country> = arrayOf()
    private var countryCode: String? = null

    private val accountRequest = AccountRequest()

    private var deepLink: String? = null
    private var fcmToken: String? = null

    private val viewModel: FinishAccountViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FinishAccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishAccountBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setupObservers()
        fetchFCMToken()

        return binding.root
    }

    private fun setUp() {
        accountRequest.locale = Locale.getDefault().language.lowercase()

        arguments?.getString(Const.EXTRA_COUNTRY_CODE).let {
            Log.d(TAG, "QQQ onCreateView: countryCode: $it")
            countryCode = it
            viewModel.loadAllCountries()
        }

        val list = arrayListOf<String>()
        for (i in 2022 downTo 1922)
            list.add(i.toString())
        //listYear = resources.getStringArray(R.array.yob)
        listYear = list.toTypedArray()
        binding.stringRuleUtil = StringRuleUtil
        binding.model = accountRequest

        val adapterYear =
            ArrayAdapter(requireActivity(), R.layout.item_yob_spinner, listYear)
        binding.spnYob.adapter = adapterYear

        binding.spnYob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                val year = listYear.getOrNull(i)
                year?.let {
                    accountRequest.birthdate = DateUtils.changeFormatFromTo(
                        oldFormat = "yyyy", year, newFormat = DateUtils.usedFormat
                    )
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        binding.lytPoweredBy.root.setOnClickListener {
            openWebUrl(Const.MAGMA_WEB_URL)
        }

        fetchDynamicLink()
    }

    private fun fetchDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(Intent())
            .addOnSuccessListener(requireActivity()) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    //deepLink = pendingDynamicLinkData.link
                    val uri = pendingDynamicLinkData.link
                    deepLink = uri?.getQueryParameter("invitedby")
                }
            }
            .addOnFailureListener(requireActivity())
            { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    private fun setupObservers() {

        viewModel.validation.observe(
            viewLifecycleOwner, EventObserver
                (object : EventObserver.EventUnhandledContent<FinishAccountValidation> {
                override fun onEventUnhandledContent(t: FinishAccountValidation) {
                    when (t) {
                        FinishAccountValidation.NAME_REQUIRED -> {
                            binding.txtInputNameLyt.error =
                                getString(R.string.name_can_not_be_empty)
                        }
                        FinishAccountValidation.BIRTH_DATE_REQUIRED -> {
                            showErrorToast(getString(R.string.date_of_birth_is_required))
                        }
                    }
                }
            })
        )

        /**Country Api*/
        viewModel.countriesDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<Country>> {
                override fun onEventUnhandledContent(t: List<Country>) {
                    if (t.isNotEmpty()) {
                        countryList = t.toTypedArray()
                        val country = t.first { it.alpha2 == countryCode }
                        accountRequest.country = country._id
                    } else {
                        viewModel.getCountries(limit = 0, offset = 0)
                    }
                }
            })
        )
        // listen to api result
        viewModel.responseCountries.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<CountriesResponse>> {
                override fun onEventUnhandledContent(t: Resource<CountriesResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            hideLoadingDialog()
                            // response is ok get the data and display it in the list
                            val response = t.response as CountriesResponse
                            Log.d("TAG", "QAQ responseCountries: $response")

                            viewModel.deleteAndSaveCountries(response.items)
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d("TAG", "QAQ response: DataError $response")
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d("TAG", "QAQ response: $response")
                        }
                    }
                }
            })
        )
        // listen to api result
        viewModel.response.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<MyAccountResponse>> {
                override fun onEventUnhandledContent(t: Resource<MyAccountResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            //binding.progress.visibility = View.GONE
                            val response = t.response as MyAccountResponse
                            Log.d("TAG", "response: $response")
                            onFetchedAccountSuccess(response)
                        }
                        is Resource.DataError -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d("TAG", "response: DataError $response")
                            //showErrorToast(response.failureMessage)
                            Toast.makeText(
                                requireActivity(), response.failureMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Resource.Exception -> {
                            //binding.progress.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d("TAG", "response: $response")
                            //showErrorToast(response.toString())
                            Toast.makeText(
                                requireActivity(), response.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
        )

        // listen to api result
        viewModel.updateResponse.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Account>> {
                override fun onEventUnhandledContent(t: Resource<Account>) {
                    hideKeyboard()
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response as Account
                            Log.d(TAG, "response: $response")
                            ContactManager.setAccount(response)
                            findNavController().navigate(
                                FinishAccountFragmentDirections
                                    .actionFinishAccountInvitationLink(deepLink)
                            )
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response Exception: $response")
                            showToast(response.toString())
                        }
                    }
                }
            })
        )
    }

    private fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (task.isSuccessful) {
                    fcmToken = task.result
                    accountRequest.firebaseFCMToken = fcmToken
                }
            }
    }

    private fun onFetchedAccountSuccess(response: MyAccountResponse) {
        ContactManager.setAccount(response)
    }

    override fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        private const val TAG = "FinishAccountFragment"
    }
}