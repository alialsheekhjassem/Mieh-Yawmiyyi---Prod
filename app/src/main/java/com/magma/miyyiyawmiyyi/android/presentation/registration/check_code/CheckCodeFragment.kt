package com.magma.miyyiyawmiyyi.android.presentation.registration.check_code

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.DynamicLink.IosParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.databinding.FragmentCheckCodeBinding
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CheckCodeFragment : ProgressBarFragments() {
    lateinit var binding: FragmentCheckCodeBinding
    private var phoneNumber: String? = null
    private var mAuth: FirebaseAuth? = null
    private var mCallbacks: OnVerificationStateChangedCallbacks? = null
    private var countDownTimer: CountDownTimer? = null
    private var mResendToken: ForceResendingToken? = null
    private lateinit var mVerificationId: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CheckCodeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CheckCodeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckCodeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        mAuth = FirebaseAuth.getInstance()
        startPhoneCallbacks()

        arguments?.getString(Const.EXTRA_PHONE_NUMBER).let {
            Log.d(TAG, "QQQ onCreateView: phoneNumber: $it")
            phoneNumber = it
            phoneNumber?.let { phone ->
                startPhoneNumberVerification(phone)
            }
        }

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.actions.observe(
            viewLifecycleOwner,
            EventObserver(
                object :
                    EventObserver.EventUnhandledContent<CheckCodeActions> {
                    override fun onEventUnhandledContent(t: CheckCodeActions) {
                        when (t) {
                            CheckCodeActions.VERIFY_CLICKED -> {
                                verifyCode()
                                /*countDownTimer?.cancel()
                                Navigation.findNavController(binding.root)
                                    .navigate(CheckCodeFragmentDirections.actionCheckCodeFinishAccountSetup())*/
                            }
                            CheckCodeActions.RESEND_CODE_CLICKED -> {
                                resendCode()
                            }
                            CheckCodeActions.EDIT_PHONE_CLICKED -> {
                                countDownTimer?.cancel()
                                findNavController().navigate(
                                    CheckCodeFragmentDirections
                                        .actionCheckCodeLogin()
                                )
                            }
                        }
                    }
                })
        )


        // listen to api result
        viewModel.loginResponse.observe(
            requireActivity(),
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<LoginResponse>> {
                override fun onEventUnhandledContent(t: Resource<LoginResponse>) {
                    hideKeyboard()
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            //showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response as LoginResponse
                            Log.d(TAG, "registerResponse: $response")
                            response.accessToken?.token?.let { viewModel.saveToken(it) }
                            response.refreshToken?.token?.let { viewModel.saveRefreshToken(it) }
                            showSuccessToast(getString(R.string.success))
                            countDownTimer?.cancel()

                            //subscribe topics
                            /*subscribeTopic(Const.TOPIC_ROUNDS)
                            subscribeTopic(Const.TOPIC_GRAND_PRIZE)
                            if (LocalHelper.locale?.language == "ar") {
                                unSubscribeTopic(Const.TOPIC_GENERAL_EN)
                                subscribeTopic(Const.TOPIC_GENERAL_AR)
                            } else {
                                unSubscribeTopic(Const.TOPIC_GENERAL_AR)
                                subscribeTopic(Const.TOPIC_GENERAL_EN)
                            }*/

                            Navigation.findNavController(binding.root)
                                .navigate(CheckCodeFragmentDirections.actionCheckCodeFinishAccountSetup())
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "registerResponse: DataError $response")
                            showToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "onEventUnhandledContent: $response")
                            showToast(response.toString())
                        }
                    }
                }
            })
        )
    }

    private fun subscribeTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "subscribeTopics: Success $topic")

                }
            }
    }

    private fun unSubscribeTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "unSubscribeTopic: Success $topic")
                }
            }
    }

    private fun resendCode() {
        binding.txtResendCode.isEnabled = false
        binding.txtResendCode.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.grey_60
            )
        )
        startTimer()
        phoneNumber?.let { phone ->
            mResendToken?.let { resendVerificationCode(phone, it) }
        }
    }

    private fun verifyCode() {
        hideKeyboard()
        if (binding.otpNumber.text != null && binding.otpNumber.text.toString().isNotEmpty()
            && binding.otpNumber.text.toString().length == 6
        ) {
            try {
                verifyPhoneNumberWithCode(mVerificationId, binding.otpNumber.text.toString())
            } catch (exception: Exception) {
                Log.d(TAG, "onCreateView: $exception")
            }
        } else {
            showErrorToast(getString(R.string.wrong_code))
        }
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: ForceResendingToken
    ) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallbacks!!) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(token) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun startPhoneCallbacks() {
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted: $credential")
                signInWithPhoneAuthCredential(credential)
            }

            @SuppressLint("SetTextI18n")
            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d(TAG, "onVerificationFailed: $e")
                context?.let {
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        showErrorToast(getString(R.string.wrong_number) + " : " + e.message)
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        showErrorToast(e.message!!)
                    } else {
                        if (e.message != null && e.message!!.contains("Error 403"))
                            showErrorToast(getString(R.string.forbidden))
                        else {
                            showErrorToast(getString(R.string.forbidden))
                        }
                    }
                }
                // Show a message and update the UI
                //alertDialog.dismiss()
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent: $token")
                countDownTimer?.cancel()

                binding.btnVerify.isEnabled = true
                startTimer()

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token

                context?.let {
                    showSuccessToast(getString(R.string.success))
                }
            }
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding?.let {
                    binding.txtResendCode.text = "${getString(R.string.resend_45s)} (${(millisUntilFinished / 1000)})"
                }
            }

            override fun onFinish() {
                binding?.let {
                    binding.txtResendCode.text = getString(R.string.resend_45s)
                    binding.txtResendCode.isEnabled = true
                    binding.txtResendCode.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.colorPrimary
                        )
                    )
                }
            }
        }.start()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = mAuth?.let {
            mCallbacks?.let { it1 ->
                PhoneAuthOptions.newBuilder(it)
                    .setPhoneNumber(phoneNumber) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity()) // Activity (for callback binding)
                    .setCallbacks(it1) // OnVerificationStateChangedCallbacks
                    .build()
            }
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        // [END start_phone_auth]
    }

    // [END resend_verification]
    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showLoadingDialog()
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(
                requireActivity()
            ) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result.user
                    if (user != null) {
                        val idToken = user.getIdToken(false).result.token
                        val userUID = user.uid
                        createLink()
                        doServerLogin(idToken, userUID)
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showErrorToast(getString(R.string.wrong_code))
                        hideLoadingDialog()
                    }
                }
            }
    }


    /**
     * create invitation  firebase link and save it
     */
    private fun createLink() {
        Log.d(TAG, "ZZZ createLink: started ")
        // [START ddl_referral_create_link]
        val user = FirebaseAuth.getInstance().currentUser!!
        val uid = user.uid
        val link = "https://com.magma.miehyawmieh.android/?invitedby=$uid"
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDomainUriPrefix("https://miehyawmieh.page.link/")
            .setAndroidParameters(
                AndroidParameters.Builder("com.magma.miehyawmieh.android")
                    .setMinimumVersion(125)
                    .build()
            )
            .setIosParameters(
                IosParameters.Builder("com.magma.miehyawmieh.ios")
                    .setAppStoreId("123456789")
                    .setMinimumVersion("1.0.1")
                    .build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener { shortDynamicLink ->
                val mInvitationUrl = shortDynamicLink.shortLink
                Log.d(TAG, "ZZZ createLink: $mInvitationUrl")
                mInvitationUrl?.let { viewModel.saveInvitationLink(mInvitationUrl.toString()) }
            }
            .addOnFailureListener { Log.d(TAG, "ZZZ createLink: $it") }
        // [END ddl_referral_create_link]
    }

    private fun doServerLogin(idToken: String?, userUID: String) {
        val loginRequest = LoginRequest()
        loginRequest.token = idToken
        loginRequest.phone = phoneNumber
        Log.d(TAG, "doServerLogin: $userUID")
        viewModel.doServerLogin(loginRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        countDownTimer?.onFinish()
    }

    companion object {
        private const val TAG = "CheckCodeFragment"
    }
}