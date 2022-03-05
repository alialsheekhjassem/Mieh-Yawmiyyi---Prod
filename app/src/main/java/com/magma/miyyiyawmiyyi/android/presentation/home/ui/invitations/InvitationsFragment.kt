package com.magma.miyyiyawmiyyi.android.presentation.home.ui.invitations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.databinding.FragmentInvitationsBinding
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class InvitationsFragment : Fragment() {

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

    private fun setUp() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
}