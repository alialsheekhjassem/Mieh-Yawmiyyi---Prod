package com.magma.miyyiyawmiyyi.android.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentOnboardingPagerBinding
import com.magma.miyyiyawmiyyi.android.presentation.registration.RegistrationActivity
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import javax.inject.Inject

class OnBoardingPagerFragment : Fragment() {

    lateinit var binding: FragmentOnboardingPagerBinding
    private lateinit var mViewPager: ViewPager2

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OnBoardingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnBoardingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //lang
        //LocalHelper.onCreate(requireActivity())
        binding = FragmentOnboardingPagerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()

        return binding.root
    }

    private fun setUp() {
        mViewPager = binding.viewPager
        mViewPager.adapter = OnBoardingViewPagerAdapter(requireActivity(), binding.root.context)
        //mViewPager.offscreenPageLimit = 1
        //mViewPager.isUserInputEnabled = false
        //TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        binding.pageIndicator.setViewPager2(mViewPager)

        binding.btnBack.setOnClickListener {
            if (mViewPager.currentItem > 0)
                mViewPager.currentItem = mViewPager.currentItem - 1
        }
        binding.btnNext.setOnClickListener {
            if (mViewPager.currentItem < 2)
                mViewPager.currentItem = mViewPager.currentItem + 1
            else {
                viewModel.setIsShowOnBoarding(true)
                goToRegisterActivity()
            }
        }
        binding.txtSkip.setOnClickListener {
            viewModel.setIsShowOnBoarding(true)
            goToRegisterActivity()
        }

        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("TAG", "onPageSelected: $position")
                if (position == 0) {
                    binding.btnBack.visibility = View.GONE
                } else {
                    binding.btnBack.visibility = View.VISIBLE
                }
                hideKeyboard()
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
}