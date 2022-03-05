package com.magma.miyyiyawmiyyi.android.presentation.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.databinding.ActivityOnboardingBinding
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OnBoardingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OnBoardingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        //lang
        LocalHelper.onCreate(this)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        supportActionBar?.hide()

    }
}