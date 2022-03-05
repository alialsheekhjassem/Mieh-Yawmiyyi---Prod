package com.magma.miyyiyawmiyyi.android.presentation.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjection
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.ActivityHomeBinding
import com.magma.miyyiyawmiyyi.android.utils.LocalHelper
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.util.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mDrawerToggle: ActionBarDrawerToggle

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        //lang
        LocalHelper.onCreate(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.appBar.toolbar
        val drawerLayout = binding.drawerLayout
        setSupportActionBar(toolbar)

        mDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout,
            toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_close
        ) {
        }
        drawerLayout.addDrawerListener(mDrawerToggle)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_tasks, R.id.navigation_tickets,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val bottomNavView: BottomNavigationView = binding.appBar.content.bottomNavView
        bottomNavView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        setupActionBarWithNavController(navController)
        NavigationUI.setupWithNavController(binding.navView, navController)

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            toolbar.toolbar_title.text = nd.label
            //enableDrawer(true)
            toolbar.setNavigationIcon(R.drawable.ic_feather_menu)
            toolbar.navigationIcon?.setTint(ActivityCompat.getColor(this, R.color.colorPrimary))

            if (nd.id == R.id.navigation_home || nd.id == R.id.navigation_live_stream ||
                /*nd.id == R.id.navigation_profile || nd.id == R.id.navigation_tasks ||*/
                nd.id == R.id.navigation_tickets
            ) {
                bottomNavView.visibility = View.VISIBLE
                binding.appBar.coordinatorLayout.setBackgroundResource(0)
                toolbar.setBackgroundResource(R.drawable.app_bar_bkg)
                toolbar.elevation = 5F
            } else {
                if (nd.id != R.id.navigation_profile && nd.id != R.id.navigation_tasks) {
                    toolbar.setNavigationIcon(R.drawable.ic_circle_filled_left)
                    bottomNavView.visibility = View.GONE
                } else {
                    bottomNavView.visibility = View.VISIBLE
                }
                toolbar.setBackgroundResource(0)
                binding.appBar.coordinatorLayout.setBackgroundResource(R.drawable.bg_grey)
                toolbar.elevation = 0F
            }
        }
        mDrawerToggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_feather_menu)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocalHelper.setLocale(this, Locale.getDefault().language)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment)
        return (navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp())
    }
}