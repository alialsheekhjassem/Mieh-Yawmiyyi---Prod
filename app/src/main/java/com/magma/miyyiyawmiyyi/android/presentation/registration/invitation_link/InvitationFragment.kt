package com.magma.miyyiyawmiyyi.android.presentation.registration.invitation_link

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject
import com.magma.miyyiyawmiyyi.android.databinding.FragmentInvitationLinkBinding
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.presentation.registration.check_code.CheckCodeFragment
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager

class InvitationFragment : ProgressBarFragments() {

    lateinit var binding: FragmentInvitationLinkBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var deepLink: String? = null

    private val viewModel: InvitationLinkViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[InvitationLinkViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvitationLinkBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        arguments?.getString(Const.EXTRA_DEEP_LINK).let {
            Log.d(TAG, "QQQ onCreateView: deepLink: $it")
            deepLink = it
            deepLink?.let { link ->
                viewModel.onDone(link)
            }
        }

        setUp()
        setupObservers()

        return binding.root
    }

    private fun setUp() {
    }

    private fun setupObservers() {

        viewModel.actions.observe(
            viewLifecycleOwner, EventObserver(
                object : EventObserver.EventUnhandledContent<InvitationLinkActions> {
                    override fun onEventUnhandledContent(t: InvitationLinkActions) {
                        when (t) {
                            InvitationLinkActions.SKIP_CLICKED -> {
                                goToHomeActivity()
                            }
                            InvitationLinkActions.EMPTY_CODE -> {
                                showErrorToast(getString(R.string.wrong_code))
                            }
                            InvitationLinkActions.WRONG_CODE -> {
                                showErrorToast(getString(R.string.wrong_code))
                            }
                            InvitationLinkActions.CHANGE_NUMBER_CLICKED -> {
                                findNavController().navigate(InvitationFragmentDirections.actionInvitationLinkLogin())
                            }
                            else -> {}
                        }
                    }
                })
        )

        // listen to api result
        viewModel.updateResponse.observe(
            requireActivity(),
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
                            goToHomeActivity()
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
        private const val TAG = "InvitationFragment"
    }
}