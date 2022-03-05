package com.magma.miyyiyawmiyyi.android.presentation.home.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magma.miyyiyawmiyyi.android.R
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentSettingsBinding
import com.magma.miyyiyawmiyyi.android.presentation.home.HomeActivity
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import java.lang.Exception
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setUp()

        return binding.root
    }

    private fun setUp() {
        val listLanguage = resources.getStringArray(R.array.languages)
        val adapterLanguage =
            ArrayAdapter(requireActivity(), R.layout.item_setting_spinner, listLanguage)
        binding.spnLanguage.adapter = adapterLanguage

        when (viewModel.getLanguage()) {
            "en" -> binding.spnLanguage.setSelection(0)
            "ar" -> binding.spnLanguage.setSelection(1)
            "tr" -> binding.spnLanguage.setSelection(2)
        }
        binding.spnLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                // Your code here
                var lang = "en"
                when (i) {
                    0 -> lang = "en"
                    1 -> lang = "ar"
                    2 -> lang = "tr"
                }

                try {
                    viewModel.getLanguage()?.let {
                        if (!viewModel.getLanguage().equals(lang)) {
                            viewModel.setLanguage(lang)
                            restartApp(lang)
                        }
                    }
                } catch (ignored: Exception) {
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

    }

    private fun restartApp(lang: String) {
        LocalHelper.setLocale(binding.root.context, lang)
        val toMainActivity = Intent(context, HomeActivity::class.java)
        toMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(toMainActivity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}