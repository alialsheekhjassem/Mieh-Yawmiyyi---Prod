package com.magma.miyyiyawmiyyi.android.presentation.details.address

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.DatePickerChildFragment
import com.magma.miyyiyawmiyyi.android.utils.TimePickerParentFragment
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentAddressBinding
import com.magma.miyyiyawmiyyi.android.utils.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddressFragment : Fragment(), View.OnClickListener,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddressBinding? = null
    private lateinit var navController: NavController
    private var currentViewId = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddressViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        navController = requireParentFragment().findNavController()

        setUpListeners()

        return binding.root
    }

    private fun setUpListeners() {
        binding.btnBack.setOnClickListener(this)
        binding.txtHour.setOnClickListener(this)
        binding.txtDay.setOnClickListener(this)
        binding.txtNumOfPeople.setOnClickListener(this)
        binding.txtTimeForFoodHour.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
        binding.txtTitleOnline.setOnClickListener(this)
        binding.txtTitlePayOnDelivery.setOnClickListener(this)
        binding.txtTitleShareBill.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                backToPreviousStep()
            }
            R.id.txt_day -> {
                showDateDialog()
            }
            R.id.txt_hour -> {
                showTimeDialog(R.id.txt_hour)
            }
            R.id.txt_num_of_people -> {
                //showTimeDialog()
            }
            R.id.txt_time_for_food_hour -> {
                showTimeDialog(R.id.txt_time_for_food_hour)
            }
            R.id.btn_continue -> {
                goToNextStep()
            }
        }
    }

    private fun backToPreviousStep() {
        if (binding.expandableLyt.isExpanded && !binding.expandableLytPay.isExpanded && !binding.expandableLytShare.isExpanded) {
            binding.btnBack.isEnabled = false
            binding.expandableLyt.collapse()
        } else if (binding.expandableLytPay.isExpanded && !binding.expandableLytShare.isExpanded) {
            binding.expandableLyt.expand()
            binding.expandableLytPay.collapse()
        } else if (binding.expandableLytShare.isExpanded) {
            binding.expandableLytPay.expand()
            binding.expandableLytShare.collapse()
        }
    }

    private fun goToNextStep() {
        if (!binding.expandableLyt.isExpanded && !binding.expandableLytPay.isExpanded && !binding.expandableLytShare.isExpanded) {
            binding.btnBack.isEnabled = true
            binding.expandableLyt.expand()
        } else if (!binding.expandableLytPay.isExpanded && !binding.expandableLytShare.isExpanded) {
            binding.btnBack.isEnabled = true
            binding.expandableLyt.collapse()
            binding.expandableLytPay.expand()
        } else if (!binding.expandableLytShare.isExpanded) {
            binding.btnBack.isEnabled = true
            binding.expandableLyt.collapse()
            binding.expandableLytPay.collapse()
            binding.expandableLytShare.expand()
        } else {
            binding.btnBack.isEnabled = false
            binding.expandableLytShare.collapse()
            Toast.makeText(binding.root.context, getString(R.string.confirm), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showDateDialog() {
        val datePicker: DialogFragment = DatePickerChildFragment()
        datePicker.setTargetFragment(this@AddressFragment, 0)
        datePicker.show(parentFragmentManager, Const.TAG_DatePickerParentFragment)
    }

    private fun showTimeDialog(id: Int) {
        currentViewId = id
        val datePicker: DialogFragment = TimePickerParentFragment()
        datePicker.setTargetFragment(this@AddressFragment, 0)
        datePicker.show(parentFragmentManager, Const.TAG_TimePickerParentFragment)
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val amPm = if (hourOfDay < 12) "AM" else "PM"
        if (currentViewId == R.id.txt_hour)
            binding.txtHour.text ="$hourOfDay:$minute$amPm"
        else if (currentViewId == R.id.txt_time_for_food_hour)
                binding.txtTimeForFoodHour.text = "$hourOfDay:$minute$amPm"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = dayOfMonth
        val tf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDateString = tf.format(c.time)
        binding.txtDay.text = currentDateString
    }
}