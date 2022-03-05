package com.magma.miyyiyawmiyyi.android.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentOnboardingBinding
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class OnBoardingFragment : Fragment() {
    private lateinit var title: String
    private lateinit var description: String
    private var imageResource = 0
    private var position = 0
    lateinit var binding: FragmentOnboardingBinding
    private var viewPager: ViewPager2? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OnBoardingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OnBoardingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = requireArguments().getInt(ARG_PARAM1)
            title = requireArguments().getString(ARG_PARAM2)!!
            description = requireArguments().getString(ARG_PARAM3)!!
            imageResource = requireArguments().getInt(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()

        return binding.root
    }

    private fun setUp() {
        binding.imgBoard.setImageResource(imageResource)
        binding.txtTitle.text = title
        binding.txtSubTitle.text = description

        when (position) {
            0 -> {
                binding.imgBoard.setBackgroundResource(R.drawable.onboard_1_bg)
                binding.imgBoard.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.imgSpeakerLeft.visibility = View.GONE
                binding.imgSpeakerRight.visibility = View.GONE
            }
            1 -> {
                binding.imgBoard.setBackgroundResource(R.drawable.onboard_2_bg)
                binding.imgBoard.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.imgSpeakerLeft.visibility = View.GONE
                binding.imgSpeakerRight.visibility = View.GONE
            }
            2 -> {
                binding.imgBoard.setBackgroundResource(0)
                binding.imgBoard.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.imgSpeakerLeft.visibility = View.VISIBLE
                binding.imgSpeakerRight.visibility = View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewPager = requireActivity().findViewById(R.id.viewPager)

        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("TAG", "onPageSelected: $position")
                hideKeyboard()
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun goToRegisterActivity() {
        val intent = Intent(requireActivity(), RegistrationActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun hideKeyboard() {
        val view = binding.root
        val imm =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
        ): OnBoardingFragment {
            val fragment = OnBoardingFragment()
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