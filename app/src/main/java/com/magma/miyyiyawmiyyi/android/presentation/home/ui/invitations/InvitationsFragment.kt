package com.magma.miyyiyawmiyyi.android.presentation.home.ui.invitations

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentInvitationsBinding
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class InvitationsFragment : ProgressBarFragments() {

    lateinit var binding: FragmentInvitationsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: InvitationsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[InvitationsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvitationsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {
        val info = ContactManager.getCurrentInfo()
        val account = ContactManager.getCurrentAccount()
        info?.let {
            binding.txtTotalPoints.text =
                "${it.userPoints?.toString()} ${getString(R.string.points)}"
            binding.txtTitlePointsCount.text =
                it.activeRound?.config?.maxTicketsPerContestant?.let { tick ->
                    "$tick ${getString(R.string.points)}"
                } ?: "0 ${getString(R.string.points)}"
            binding.txtInvitationLink.text =
                viewModel.getInvitationLink() ?: "http//:sndn-test-100yawmiyyi"
        }
        account?.let {
            binding.txtSentCardsNum.text = it.sentInvites?.toString()
            binding.txtReceivedCardsNum.text = it.fulfilledInvites?.toString()
            binding.txtInvitationCode.text = it.account?.invite?.code ?: "712 342"
        }

        binding.txtInvitationLink.setOnClickListener { copyText(binding.txtInvitationLink.text.toString()) }
        binding.txtInvitationCode.setOnClickListener { copyText(binding.txtInvitationCode.text.toString()) }

        /*Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        private const val TAG = "InvitationFragment"
    }
}