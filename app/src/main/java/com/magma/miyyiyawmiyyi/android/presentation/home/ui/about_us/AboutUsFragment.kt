package com.magma.miyyiyawmiyyi.android.presentation.home.ui.about_us

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentAboutUsBinding
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)

        binding.btnShareApp.setOnClickListener {
            val intentInvite = Intent(Intent.ACTION_SEND)
            intentInvite.type = "text/plain"
            val body = getString(R.string.app_link)
            val subject = resources.getString(R.string.about_app)
            intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject)
            intentInvite.putExtra(Intent.EXTRA_TEXT, body)
            startActivity(Intent.createChooser(intentInvite, "Share using"))
        }

        return binding.root
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