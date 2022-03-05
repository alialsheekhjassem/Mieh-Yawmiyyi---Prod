package com.magma.miyyiyawmiyyi.android.presentation.registration.finish_account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
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

    private val viewViewModel: FinishAccountViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FinishAccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishAccountBinding.inflate(inflater, container, false)
        binding.viewModel = viewViewModel

        setUp()
        setupObservers()

        return binding.root
    }

    private fun setUp() {
        binding.stringRuleUtil = StringRuleUtil

        val listYear = resources.getStringArray(R.array.yob)
        val adapterYear =
            ArrayAdapter(requireActivity(), R.layout.item_yob_spinner, listYear)
        binding.spnYob.adapter = adapterYear
    }

    private fun setupObservers() {

        viewViewModel.actions.observe(
            viewLifecycleOwner, EventObserver(
                object : EventObserver.EventUnhandledContent<FinishAccountActions> {
                    override fun onEventUnhandledContent(t: FinishAccountActions) {
                        when (t) {
                            FinishAccountActions.DONE_CLICKED -> {
                                findNavController().navigate(FinishAccountFragmentDirections
                                    .actionFinishAccountInvitationLink())
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