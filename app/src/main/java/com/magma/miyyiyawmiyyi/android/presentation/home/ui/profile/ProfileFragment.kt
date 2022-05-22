package com.magma.miyyiyawmiyyi.android.presentation.home.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.AccountRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.CountriesResponse
import com.magma.miyyiyawmiyyi.android.databinding.DialogUpdatePhoneNumberBinding
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentProfileBinding
import com.magma.miyyiyawmiyyi.android.model.Account
import com.magma.miyyiyawmiyyi.android.model.Country
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.DateUtils
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileFragment : ProgressBarFragments() {

    lateinit var binding: FragmentProfileBinding
    private var dialogBinding: DialogUpdatePhoneNumberBinding? = null
    private var alertDialogUpdatePhone: AlertDialog? = null
    private var phoneNumber: String? = null
    private var mAuth: FirebaseAuth? = null
    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var countDownTimer: CountDownTimer? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var mVerificationId: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val accountRequest = AccountRequest()

    var listYear: Array<String> = arrayOf()

    var countryList: Array<Country> = arrayOf()
    private var selectedCountry: Country? = null

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()
        setupObservers()

        return binding.root
    }

    private fun setUp() {
        mAuth = FirebaseAuth.getInstance()
        val account = ContactManager.getCurrentAccount()?.account
        startPhoneCallbacks()
        accountRequest.locale = Locale.getDefault().language.lowercase()
        binding.model = accountRequest

        binding.countryPicker.registerCarrierNumberEditText(binding.edtPhoneNumber)

        binding.txtVerify.setOnClickListener {
            phoneNumber = binding.countryPicker.fullNumberWithPlus
            if (phoneNumber.isNullOrEmpty()) {
                showErrorToast(getString(R.string.please_enter_avalid_phone))
            } else if (account?.phone.equals(phoneNumber)) {
                showErrorToast(getString(R.string.please_enter_different_phone))
            } else {
                showUpdatePhoneNumberDialog()
            }
        }

        val list = arrayListOf<String>()
        for (i in 2022 downTo 1922)
            list.add(i.toString())
        //listYear = resources.getStringArray(R.array.yob)
        listYear = list.toTypedArray()

        val adapterYear =
            ArrayAdapter(requireActivity(), R.layout.item_yob_spinner_profile, listYear)
        binding.spnYob.adapter = adapterYear

        binding.spnYob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                val year = listYear.getOrNull(i)
                accountRequest.birthdate = year
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        binding.spnLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                val country = countryList.getOrNull(i)
                selectedCountry = country
                accountRequest.country = country?._id
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        account?.let {
            accountRequest.name = it.name
            binding.edtFullName.setText(it.name)
            binding.edtPhoneNumber.setText(it.phone)

            it.info?.birthdate?.let { birthDate ->
                val year = DateUtils.changeFormatFromTo(
                    oldFormat = DateUtils.usedFormat,
                    birthDate, newFormat = "yyyy"
                )
                val y = list.filter { s -> year == s }
                val position = adapterYear.getPosition(y.first())
                binding.spnYob.setSelection(position)
            }
        }
    }

    private fun setupObservers() {

        viewModel.validation.observe(
            viewLifecycleOwner, EventObserver
                (object : EventObserver.EventUnhandledContent<ProfileValidation> {
                override fun onEventUnhandledContent(t: ProfileValidation) {
                    when (t) {
                        ProfileValidation.NAME_REQUIRED -> {
                            showErrorToast(getString(R.string.name_can_not_be_empty))
                        }
                        ProfileValidation.BIRTH_DATE_REQUIRED -> {
                            showErrorToast(getString(R.string.date_of_birth_is_required))
                        }
                        else -> {}
                    }
                }
            })
        )

        // listen to api result
        viewModel.updateResponse.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Account>> {
                override fun onEventUnhandledContent(t: Resource<Account>) {
                    hideKeyboard()
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response as Account
                            Log.d(TAG, "response: $response")
                            ContactManager.setAccount(response)
                            showSuccessToast(getString(R.string.updated))
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response Exception: $response")
                            showToast(response.toString())
                        }
                    }
                }
            })
        )

        viewModel.countriesDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<Country>> {
                override fun onEventUnhandledContent(t: List<Country>) {
                    if (t.isNotEmpty()) {
                        countryList = t.toTypedArray()
                        val adapterCountry =
                            ArrayAdapter(requireActivity(), R.layout.item_yob_spinner_profile, t)
                        binding.spnLocation.adapter = adapterCountry

                        val accountCountry =
                            ContactManager.getCurrentAccount()?.account?.info?.country
                        if (accountCountry != null) {
                            val country = t.first { cot -> cot._id == accountCountry._id }
                            val index = adapterCountry.getPosition(country)
                            Log.d(TAG, "onEventUnhandledContent: $accountCountry = $index")
                            binding.spnLocation.setSelection(index)
                        }
                    } else {
                        viewModel.getCountries(limit = 20, offset = 0)
                    }
                }
            })
        )
        // listen to api result
        viewModel.response.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<CountriesResponse>> {
                override fun onEventUnhandledContent(t: Resource<CountriesResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            val response = t.response as CountriesResponse
                            Log.d(TAG, "response: $response")

                            viewModel.deleteAndSaveCountries(response)
                        }
                        is Resource.DataError -> {
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                        }
                        is Resource.Exception -> {
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                        }
                    }
                }
            })
        )
    }

    private fun showUpdatePhoneNumberDialog() {
        val builder = AlertDialog.Builder(requireContext())
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()), R.layout.dialog_update_phone_number, null, false
        )
        builder.setView(dialogBinding?.root)
        alertDialogUpdatePhone = builder.create()
        dialogBinding?.btnVerify?.setOnClickListener {
            verifyCode(dialogBinding!!)
        }
        alertDialogUpdatePhone?.setCancelable(true)
        alertDialogUpdatePhone?.show()
        Log.d(TAG, "showUpdatePhoneNumberDialog: $phoneNumber")
        startPhoneNumberVerification(phoneNumber!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.loadAllCountries()
    }

    private fun verifyCode(dialogBinding: DialogUpdatePhoneNumberBinding) {
        hideKeyboard()
        if (dialogBinding.otpNumber.text != null && dialogBinding.otpNumber.text.toString()
                .isNotEmpty()
            && dialogBinding.otpNumber.text.toString().length == 6
        ) {
            try {
                dialogBinding.btnVerify.isEnabled = false
                verifyPhoneNumberWithCode(mVerificationId, dialogBinding.otpNumber.text.toString())
            } catch (exception: Exception) {
                Log.d(TAG, "onCreateView: $exception")
            }
        } else {
            showErrorToast(getString(R.string.wrong_code))
        }
    }

    private fun startPhoneCallbacks() {
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
                alertDialogUpdatePhone?.dismiss()
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
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent: $token")
                countDownTimer?.cancel()

                dialogBinding?.btnVerify?.isEnabled = true

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token

                context?.let {
                    showSuccessToast(getString(R.string.success))
                }
            }
        }
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
        //showLoadingDialog()
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
                        doServerUpdatePhoneNumber(idToken, userUID)
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

    private fun doServerUpdatePhoneNumber(idToken: String?, userUID: String) {
        val accountPhoneRequest = AccountRequest()
        accountPhoneRequest.birthdate = null
        accountPhoneRequest.locale = null
        accountPhoneRequest.name = null
        accountPhoneRequest.phone = phoneNumber
        accountPhoneRequest.token = idToken
        viewModel.onDoneUpdatePhone(accountPhoneRequest)
        alertDialogUpdatePhone?.dismiss()
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}