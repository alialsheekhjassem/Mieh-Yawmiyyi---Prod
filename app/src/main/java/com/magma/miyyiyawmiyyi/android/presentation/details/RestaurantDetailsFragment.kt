package com.magma.miyyiyawmiyyi.android.presentation.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentRestaurantDetailsBinding
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class RestaurantDetailsFragment : Fragment() {

    private var _binding: FragmentRestaurantDetailsBinding? = null

    private var manager: FragmentManager? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: RestaurantDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RestaurantDetailsViewModel::class.java)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        manager = childFragmentManager
        replaceBody()

        return binding.root
    }

    private fun replaceBody() {
        manager!!.beginTransaction()
            .replace(
                R.id.nav_host_service, FoodMenuFragment(),
                Const.TAG_FoodMenuFragment
            )
            .commit()
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