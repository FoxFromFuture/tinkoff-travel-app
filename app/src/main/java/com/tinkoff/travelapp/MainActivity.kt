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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.tinkoff.travelapp.adapter.TripCardAdapter
import com.tinkoff.travelapp.entry.SignInActivity
import com.tinkoff.travelapp.map.NoRouteMapActivity
import com.tinkoff.travelapp.database.DBManager
import com.tinkoff.travelapp.database.model.TripDataModel
import com.tinkoff.travelapp.menu.FAQActivity
import com.tinkoff.travelapp.menu.MyAccountActivity
import com.tinkoff.travelapp.menu.SettingsActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var imagesList = mutableListOf<Int>()
    private var durationsList = mutableListOf<TripDurations>()
    private lateinit var adapter: TripCardAdapter
    private lateinit var tripPager: ViewPager2
    lateinit var viewModel: TripCardViewModel
    val dbManager = DBManager(this)
    var tripList = mutableListOf<TripDataModel>()
    var tripNameBuffer: String = ""
    var tripDateBuffer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter =
            TripCardAdapter(imagesList, durationsList)
        tripPager = findViewById(R.id.main_trip_pager)

        dbManager.openDb()
        getDbData()
        viewModel = ViewModelProvider(this)[TripCardViewModel::class.java]

        viewModel.tripDataList.observe(this) { list ->
            if (list.isSuccessful) {
                Log.d("Main", list.body().toString())
                tripPager.adapter = adapter
                list.body()?.let {
                    dbManager.writeDbData(tripNameBuffer, tripDateBuffer, it)
                    addToTripList(dbManager.readDbData())
                }
            } else {
                Toast.makeText(this, list.code(), Toast.LENGTH_SHORT).show()
            }
        }

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

        val buttonMenu = findViewById<ImageButton>(R.id.main_menu_button)
        buttonMenu.setOnClickListener(this)

        val buttonCreateTravel = findViewById<FloatingActionButton>(R.id.main_add_travel_button)
        buttonCreateTravel.setOnClickListener(this)

        val buttonLogout = findViewById<Button>(R.id.main_menu_logout_button)
        buttonLogout.setOnClickListener(this)

        val buttonMap = findViewById<ImageButton>(R.id.main_map_button)
        buttonMap.setOnClickListener(this)
    }

    private fun getDbData() {
        tripList = dbManager.readDbData()
        adapter.setListOfRoutes(tripList)
        tripPager.adapter = adapter
    }

    private fun addToTripList(list: ArrayList<TripDataModel>) {
        for (item in list) {
            tripList.add(item)
        }
        adapter.setListOfRoutes(tripList)
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
            R.id.main_map_button -> {
                val intent = Intent(this, NoRouteMapActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }

    fun dialogFragmentListener(
        tripName: String,
        tripCategories: List<String>,
        tripCost: Int,
        tripStartTime: String,
        tripEndTime: String,
        tripDuration: TripDurations
    ) {
        val date = "$tripStartTime - $tripEndTime"
        tripNameBuffer = tripName
        tripDateBuffer = date
        durationsList.add(tripDuration)
        viewModel.getRoute(tripCategories, tripStartTime, tripEndTime, tripCost)
    }
}
