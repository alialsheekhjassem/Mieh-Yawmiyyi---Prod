package com.magma.miyyiyawmiyyi.android.presentation.registration.invitation_link

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject
import com.magma.miyyiyawmiyyi.android.databinding.FragmentInvitationLinkBinding
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments

class InvitationFragment : ProgressBarFragments() {

    lateinit var binding: FragmentInvitationLinkBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: InvitationLinkViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[InvitationLinkViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvitationLinkBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

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
                            InvitationLinkActions.DONE_CLICKED -> {
                                findNavController().navigate(R.id.navigation_invitation_link)
                            }
                            InvitationLinkActions.CHANGE_NUMBER_CLICKED -> {
                                findNavController().navigate(InvitationFragmentDirections.actionInvitationLinkLogin())
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