package com.magma.miyyiyawmiyyi.android.presentation.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.magma.miyyiyawmiyyi.android.R

class OnBoardingViewPagerAdapter(fragmentActivity: FragmentActivity, private val context: Context) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFragment.newInstance(
                0,
                title = context.resources.getString(R.string.welcome_title_1),
                description = context.resources.getString(R.string.welcome_text_1),
                R.drawable.onboard_1
            )
            1 -> OnBoardingFragment.newInstance(
                1,
                title = context.resources.getString(R.string.welcome_title_2),
                description = context.resources.getString(R.string.welcome_text_2),
                R.drawable.onboard_2
            )
            else -> OnBoardingFragment.newInstance(
                2,
                title = context.resources.getString(R.string.welcome_title_3),
                description = context.resources.getString(R.string.welcome_text_3),
                R.drawable.onboard_3
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}