package com.magma.miyyiyawmiyyi.android.presentation.base

import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity
import com.magma.miyyiyawmiyyi.android.utils.BindingUtils.hideKeyboard
import com.magma.miyyiyawmiyyi.android.utils.CommonUtils
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

/**
 * Class extends MessagesFragments, handles loading processes.
 * */
open class ProgressBarFragments : Fragment() {

    private lateinit var alertDialog: AlertDialog

    fun showLoadingDialog() {
        alertDialog = CommonUtils.showLoadingDialog(requireActivity())
    }

    fun hideLoadingDialog() {
        alertDialog.cancel()
    }

    fun goToHomeActivity() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun showErrorToast(error: String){
        MotionToast.darkToast(
            requireActivity(),
            getString(R.string.error),
            error,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireActivity(), R.font.product_sans_regular)
        )
    }

    fun showSuccessToast(success: String){
        MotionToast.darkToast(
            requireActivity(),
            getString(R.string.success),
            success,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireActivity(), R.font.product_sans_regular)
        )
    }

    fun showToast(success: String){
        Toast.makeText(requireActivity(), success, Toast.LENGTH_LONG).show()
    }

    open fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
}