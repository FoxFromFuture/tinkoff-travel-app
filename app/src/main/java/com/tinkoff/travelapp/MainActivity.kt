package com.tinkoff.travelapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.tinkoff.travelapp.adapter.TripCardAdapter
import com.tinkoff.travelapp.menu.FAQActivity
import com.tinkoff.travelapp.menu.MyAccountActivity
import com.tinkoff.travelapp.menu.SettingsActivity
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.route.RouteItem

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var imagesList = mutableListOf<Int>()
    private var titleList = mutableListOf<String>()
    private var dateList = mutableListOf<String>()
    private lateinit var adapter: TripCardAdapter
    private lateinit var tripPager: ViewPager2
    lateinit var viewModel: TripCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[TripCardViewModel::class.java]

        viewModel.tripDataList.observe(this) { list ->
            if (list.isSuccessful) {
                Log.d("Main", list.body().toString())
                list.body()?.let { adapter.setListOfRoutes(it, Route()) }
            } else {
                Toast.makeText(this, list.code(), Toast.LENGTH_SHORT).show()
            }
        }

        adapter =
            TripCardAdapter(imagesList, titleList, dateList)
        tripPager = findViewById(R.id.main_trip_pager)
        tripPager.adapter = adapter


        val buttonMenu = findViewById<ImageButton>(R.id.main_menu_button)
        buttonMenu.setOnClickListener(this)

        val drawer = findViewById<DrawerLayout>(R.id.main_drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.main_navigation)
        val toggle = ActionBarDrawerToggle(this, drawer, 0, 0)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_my_account_button -> {
                    val intent = Intent(this, MyAccountActivity::class.java)
                    startActivity(intent)
                }
                R.id.main_menu_settings_button -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.main_menu_faq_button -> {
                    val intent = Intent(this, FAQActivity::class.java)
                    startActivity(intent)
                }
            }

            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val buttonCreateTravel = findViewById<FloatingActionButton>(R.id.main_add_travel_button)
        buttonCreateTravel.setOnClickListener(this)

        val buttonLogout = findViewById<Button>(R.id.main_menu_logout_button)
        buttonLogout.setOnClickListener(this)

        val buttonMap = findViewById<ImageButton>(R.id.main_global_button)
        buttonMap.setOnClickListener(this)
    }

    private fun addToList(title: String, date: String) {
        titleList.add(title)
        dateList.add(date)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.main_menu_button -> {
                val navigation = findViewById<DrawerLayout>(R.id.main_drawer_layout)
                if (navigation.isDrawerOpen(GravityCompat.START)) {
                    navigation.closeDrawer(GravityCompat.START)
                } else {
                    navigation.openDrawer(GravityCompat.START)
                }
            }
            R.id.main_add_travel_button -> {
                val fragmentManager = (view.context as FragmentActivity).supportFragmentManager
                CreateTravelDialogFragment().show(
                    fragmentManager, CreateTravelDialogFragment.TAG
                )
            }
            R.id.main_menu_logout_button -> {
                val intent = Intent(this, SignInActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
            R.id.main_global_button -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun dialogFragmentListener(
        trip_name: String,
        trip_categories: List<String>,
        trip_cost: Int,
        trip_start_time: String,
        trip_end_time: String
    ) {
        val date = "$trip_start_time - $trip_end_time"
        addToList(trip_name, date)
        viewModel.getRoute(trip_categories, trip_start_time, trip_end_time, trip_cost)
    }
}
