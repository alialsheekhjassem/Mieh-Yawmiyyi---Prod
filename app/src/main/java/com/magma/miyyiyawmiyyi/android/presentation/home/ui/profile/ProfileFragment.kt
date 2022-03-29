package com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentProfileBinding
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.DateUtils
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import java.util.*
import javax.inject.Inject

class ProfileFragment : ProgressBarFragments() {

    lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val accountRequest = AccountRequest()

    var listYear: Array<String> = arrayOf()

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setupObservers()

        return binding.root
    }

    private fun setUp() {
        accountRequest.locale = Locale.getDefault().language.lowercase()
        binding.model = accountRequest

        binding.countryPicker.registerCarrierNumberEditText(binding.edtPhoneNumber)

        val list = arrayListOf<String>()
        for (i in 1922..2022)
            list.add(i.toString())
        //listYear = resources.getStringArray(R.array.yob)
        listYear = list.toTypedArray()

        val adapterYear =
            ArrayAdapter(requireActivity(), R.layout.item_yob_spinner_profile, listYear)
        binding.spnYob.adapter = adapterYear

        binding.spnYob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                val year = listYear.getOrNull(i)
                accountRequest.birthdate = year
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        val account = ContactManager.getCurrentAccount()?.account
        account?.let {
            accountRequest.name = it.name
            binding.edtFullName.setText(it.name)
            binding.edtPhoneNumber.setText(it.phone)

            it.info?.birthdate?.let { birthDate ->
                val year = DateUtils.changeFormatFromTo(
                    oldFormat = DateUtils.usedFormat,
                    birthDate, newFormat = "yyyy"
                )
                val y = list.filter { s -> year == s }
                val position = adapterYear.getPosition(y.first())
                binding.spnYob.setSelection(position)
            }
        }
    }

    private fun setupObservers() {

        viewModel.validation.observe(
            viewLifecycleOwner, EventObserver
                (object : EventObserver.EventUnhandledContent<ProfileValidation> {
                override fun onEventUnhandledContent(t: ProfileValidation) {
                    when (t) {
                        ProfileValidation.NAME_REQUIRED -> {
                            showErrorToast(getString(R.string.name_can_not_be_empty))
                        }
                        ProfileValidation.BIRTH_DATE_REQUIRED -> {
                            showErrorToast(getString(R.string.date_of_birth_is_required))
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}