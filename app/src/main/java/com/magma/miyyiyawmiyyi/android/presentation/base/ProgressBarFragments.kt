package com.magma.miyyiyawmiyyi.android.presentation.base

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    private var alertDialog: AlertDialog? = null

    fun showLoadingDialog() {
        alertDialog = CommonUtils.showLoadingDialog(requireActivity())
    }

    fun hideLoadingDialog() {
        alertDialog?.cancel()
    }

    fun goToHomeActivity() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun showErrorToast(error: String) {
        MotionToast.darkToast(
            requireActivity(),
            getString(R.string.error),
            getErrorTranslatedToast(error),
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireActivity(), R.font.product_sans_regular)
        )

        /*val inflater = layoutInflater
        val layout: View = inflater.inflate(
            R.layout.toast,
            requireActivity().findViewById(R.id.toast_layout_root) as ViewGroup?
        )
        layout.findViewById<TextView>(R.id.text).text = error
        val toast = Toast(requireActivity())
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()*/
    }

    fun showSuccessToast(success: String) {
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

    private fun getErrorTranslatedToast(error: String): String {
        return when {
            error.lowercase().contains("No active round".lowercase()) -> {
                getString(R.string.no_active_round)
            }
            error.lowercase().contains("reached allowance of max tickets".lowercase()) -> {
                getString(R.string.reach_max_tickets)
            }
            error.lowercase().contains("No code available for the purchase".lowercase()) -> {
                getString(R.string.no_code_available)
            }
            error.lowercase().contains("Not found".lowercase()) -> {
                getString(R.string.not_found)
            }
            error.lowercase().contains("Invite code can't be changed. user already has invitedBy code".lowercase()) -> {
                getString(R.string.invite_code_exists)
            }
            error.lowercase().contains("Incorrect credentials".lowercase()) -> {
                getString(R.string.incorrect_credentials)
            }
            else -> {
                error
            }
        }
    }

    fun showToast(success: String) {
        Toast.makeText(requireActivity(), success, Toast.LENGTH_LONG).show()
    }

    fun copyText(text: String) {
        val clipboard: ClipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(android.R.attr.label.toString(), text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireActivity(), getString(R.string.copied), Toast.LENGTH_SHORT)
            .show()
    }

    fun openFacebookUrl(url: String) {
        val intent = try {
            requireActivity().packageManager.getPackageInfo("com.facebook.katana", 0)
            Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$url"))
        } catch (e: Exception) {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"))
        }
        startActivity(intent)
    }

    fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    open fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }
}