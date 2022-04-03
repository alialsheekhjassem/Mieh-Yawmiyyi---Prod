package com.magma.miyyiyawmiyyi.android.presentation.home.ui.confirm_task

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.magma.miyyiyawmiyyi.android.databinding.FragmentConfirmEndTaskBinding
import com.magma.miyyiyawmiyyi.android.utils.OnConfirmTaskListener
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ConfirmEndTaskFragment : DialogFragment() {

    private var _binding: FragmentConfirmEndTaskBinding? = null
    private lateinit var onConfirmTaskListener: OnConfirmTaskListener

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmEndTaskBinding.inflate(inflater, container, false)

        binding.yesTV.setOnClickListener { //sometimes took to long to get back to the app or the user exit it immediately
            try {
                onConfirmTaskListener.onConfirm()
                dismiss()
            } catch (ignored: Exception) {
            }
        }
        binding.noTV.setOnClickListener { dismiss() }

        return binding.root
    }

    fun setOnConfirmTaskListener(onConfirmTaskListener: OnConfirmTaskListener?) {
        this.onConfirmTaskListener = onConfirmTaskListener!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ConfirmEndTaskFragment"
    }
}