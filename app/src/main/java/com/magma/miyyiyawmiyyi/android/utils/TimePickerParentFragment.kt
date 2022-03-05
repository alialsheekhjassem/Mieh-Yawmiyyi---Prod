package com.magma.miyyiyawmiyyi.android.utils

import android.app.TimePickerDialog.OnTimeSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerParentFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        val min = c[Calendar.MINUTE]
        return TimePickerDialog(requireActivity(), targetFragment as OnTimeSetListener, hour, min, false)
    }

}