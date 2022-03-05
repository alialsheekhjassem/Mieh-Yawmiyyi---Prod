package com.magma.miyyiyawmiyyi.android.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentGetStartedBinding
import javax.inject.Inject

class GetStartedFragment : Fragment() {

    lateinit var binding: FragmentGetStartedBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OnBoardingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnBoardingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()

        return binding.root
    }

    private fun setUp() {

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.onboardFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"
        private const val ARG_PARAM4 = "param4"
        fun newInstance(
            position: Int,
            title: String?,
            description: String?,
            imageResource: Int
        ): GetStartedFragment {
            val fragment = GetStartedFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, position)
            args.putString(ARG_PARAM2, title)
            args.putString(ARG_PARAM3, description)
            args.putInt(ARG_PARAM4, imageResource)
            fragment.arguments = args
            return fragment
        }
    }
}