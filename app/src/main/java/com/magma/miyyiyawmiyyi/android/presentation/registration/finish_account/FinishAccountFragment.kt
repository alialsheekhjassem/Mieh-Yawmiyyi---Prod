package com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.MyAccountResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject
import com.magma.miyyiyawmiyyi.android.databinding.FragmentFinishAccountBinding
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.StringRuleUtil

class FinishAccountFragment : ProgressBarFragments() {

    lateinit var binding: FragmentFinishAccountBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val accountRequest = AccountRequest()

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

        return binding.root
    }

    private fun setUp() {
        binding.stringRuleUtil = StringRuleUtil
        binding.model = accountRequest

        val listYear = resources.getStringArray(R.array.yob)
        val adapterYear =
            ArrayAdapter(requireActivity(), R.layout.item_yob_spinner, listYear)
        binding.spnYob.adapter = adapterYear
    }

    private fun setupObservers() {

        viewModel.actions.observe(
            viewLifecycleOwner, EventObserver(
                object : EventObserver.EventUnhandledContent<FinishAccountActions> {
                    override fun onEventUnhandledContent(t: FinishAccountActions) {
                        when (t) {
                            FinishAccountActions.DONE_CLICKED -> {

                            }
                        }
                    }
                })
        )

        viewModel.validation.observe(
            viewLifecycleOwner, EventObserver
                (object : EventObserver.EventUnhandledContent<FinishAccountValidation> {
                override fun onEventUnhandledContent(t: FinishAccountValidation) {
                    when (t) {
                        FinishAccountValidation.NAME_REQUIRED -> {
                            binding.txtInputNameLyt.error = getString(R.string.name_can_not_be_empty)
                        }
                        FinishAccountValidation.BIRTH_DATE_REQUIRED -> {
                            showErrorToast(getString(R.string.date_of_birth_is_required))
                        }
                    }
                }
            })
        )

        // listen to api result
        viewModel.updateResponse.observe(
            requireActivity(),
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<MyAccountResponse>> {
                override fun onEventUnhandledContent(t: Resource<MyAccountResponse>) {
                    hideKeyboard()
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response as MyAccountResponse
                            Log.d(TAG, "response: $response")
                            findNavController().navigate(
                                FinishAccountFragmentDirections
                                    .actionFinishAccountInvitationLink()
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