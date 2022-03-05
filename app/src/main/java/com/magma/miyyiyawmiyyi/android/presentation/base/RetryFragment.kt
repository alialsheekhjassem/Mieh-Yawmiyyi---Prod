package com.magma.miyyiyawmiyyi.android.presentation.base

import android.app.Dialog
import android.widget.TextView
import com.magma.miyyiyawmiyyi.android.R

/**
 * Class extends ProgressBarFragments, handles Retry to do an action dialog.
 * */
abstract class RetryFragment : ProgressBarFragments() {

    var isOpen = false;

    fun showRetryMessage(message: String? = requireContext().getString(R.string.no_internet)){
        if(!isOpen) {
            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.retry_dialog)
            dialog.setCancelable(true)
            val contentMessage = dialog.findViewById<TextView>(R.id.content_text_view)
            contentMessage.text = message?:"Null"
            val retryBtn = dialog.findViewById<TextView>(R.id.retry_btn)
            retryBtn.setOnClickListener {
                dialog.cancel()
                onRetryClicked()
            }
            dialog.setOnCancelListener { isOpen = false }
            dialog.show()
            isOpen = true
        }
    }

    open fun onRetryClicked(){

    }

}