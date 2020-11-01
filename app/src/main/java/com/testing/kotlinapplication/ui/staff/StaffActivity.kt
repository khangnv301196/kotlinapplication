package com.testing.kotlinapplication.ui.staff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.testing.kotlinapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_staff.*

class StaffActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)
        setupNavigation()
    }

    private fun setupNavigation() {
        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottom_staff, navHostFragment.navController)


        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.satisticFragment,
                R.id.orderStaffFragment,
                R.id.profilestaff
            )
        )
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
            navController,
            appBarConfiguration
        )


        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    fun resetActionBar() {
        setSupportActionBar(toolbar)
    }

    fun showBottomNavigation() {
        bottom_staff.visibility = View.VISIBLE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun hideBottomNavigation() {
        bottom_staff.visibility = View.GONE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showAppBar() {
        toolbar.visibility = View.VISIBLE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    fun hideAppBar() {
        toolbar.visibility = View.GONE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    fun setTitle(title: String) {
        supportActionBar?.setTitle(title)
    }

}