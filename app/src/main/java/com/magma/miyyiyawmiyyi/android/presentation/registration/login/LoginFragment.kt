package com.magma.miyyiyawmiyyi.android.presentation.registration.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentLoginBinding
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments

class LoginFragment : ProgressBarFragments() {

    lateinit var binding: FragmentLoginBinding
    private var fcmToken: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setupObservers()

        return binding.root
    }

    private fun setUp() {
        binding.countryPicker.registerCarrierNumberEditText(binding.edtPhoneNumber)

        fetchFCMToken()

    }

    private fun setupObservers() {

        viewModel.actions.observe(
            viewLifecycleOwner, EventObserver(
                object : EventObserver.EventUnhandledContent<LoginActions> {
                    override fun onEventUnhandledContent(t: LoginActions) {
                        when (t) {
                            LoginActions.CHECK_CODE_CLICKED -> {
                                val fullNumber = binding.countryPicker.fullNumberWithPlus
                                Log.d(TAG, "QQQ onCreateView: phoneNumber: $fullNumber")
                                findNavController().navigate(LoginFragmentDirections.actionLoginToCheckCode(fullNumber))
                            }
                        }
                    }
                })
        )

        viewModel.loginValidation.observe(viewLifecycleOwner, { code ->
            val message: String = when (code) {
                Const.ERROR_EMPTY -> getString(R.string.field_can_not_be_empty)
                Const.ERROR_PHONE -> getString(R.string.phone_format_not_correct)
                else -> getString(R.string.password_format_not_correct)
            }
            showErrorToast(message)
        })
    }

    override fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (task.isSuccessful) {
                    fcmToken = task.result
                    binding.fcmToken = fcmToken
                }
            }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}