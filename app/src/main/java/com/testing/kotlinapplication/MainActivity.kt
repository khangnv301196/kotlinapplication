package com.testing.kotlinapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.testing.kotlinapplication.repository.action.ShopRepository
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()

        ShopRepository.getCardByUserID(this, Preference(this).getValueInt(Constant.USER_ID))
            .observe(this,
                Observer {
                    if (it != null) {
                        if (Preference(this).getValueInt(Constant.CART_ID) == 0) {
                            it.Id?.let { it1 -> Preference(this).save(Constant.CART_ID, it1) }
                        }
                    }
                })
    }

    private fun setupNavigation() {
        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)


        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.recentFragment,
                R.id.categoryFragment,
                R.id.helpFragment,
                R.id.profileFragment
            )
        )
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
            navController,
            appBarConfiguration
        )


        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

    }

    fun showBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.action_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//            R.id.cart_menu -> {
//                navController.navigate(R.id.cardFragment)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

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

    fun hideSoftKeyBoard() {
        var inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun setUpUI(view: View) {
        if (!(view is EditText)) {
            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    hideSoftKeyBoard();
                    return false;
                }
            })
        }
    }

    fun setUIGroup(viewGroup: ViewGroup) {
        for (i in 0..viewGroup.childCount) {
            var innerView = viewGroup.getChildAt(i)
            setUpUI(innerView)
        }
    }

}
