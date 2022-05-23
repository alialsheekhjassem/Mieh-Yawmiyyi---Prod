package com.magma.miyyiyawmiyyi.android.presentation.registration

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CountriesResponse
import com.magma.miyyiyawmiyyi.android.databinding.ActivityRegisterationBinding
import com.magma.miyyiyawmiyyi.android.model.Country
import com.magma.miyyiyawmiyyi.android.presentation.base.BaseActivity
import com.magma.miyyiyawmiyyi.android.presentation.registration.login.LoginViewModel
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class RegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterationBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        supportActionBar?.hide()

        setUp()

    }

    private fun setUp() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navController.addOnDestinationChangedListener { _, _, _ ->
            hideKeyboard()
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_login,
                R.id.navigation_check_code
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        ContactManager.getCurrentAccount()?.account?.let {
            if (!viewModel.getApiToken().isNullOrEmpty() &&
                !it.phone.isNullOrEmpty() && it.name.isNullOrEmpty()){
                navController.navigate(R.id.navigation_finish_account)
            }
        }
    }

    private fun Activity.hideKeyboard() {
        binding.root.let { this.hideKeyboard(it) }
    }

    /*private fun getItem(): Int {
        return mViewPager.currentItem
    }*/
}