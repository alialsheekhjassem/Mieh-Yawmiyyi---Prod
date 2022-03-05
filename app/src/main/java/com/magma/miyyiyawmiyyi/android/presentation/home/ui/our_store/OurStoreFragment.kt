package com.magma.miyyiyawmiyyi.android.presentation.home.ui.our_store

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.databinding.FragmentOurStoreBinding
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class OurStoreFragment : Fragment() {

    lateinit var binding: FragmentOurStoreBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

        return binding.root
    }

    private fun setUp() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
}