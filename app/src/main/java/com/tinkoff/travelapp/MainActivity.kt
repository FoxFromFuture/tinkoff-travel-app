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
import com.tinkoff.travelapp.database.DBManager
import com.tinkoff.travelapp.database.model.TripDataModel
import com.tinkoff.travelapp.entry.SignInActivity
import com.tinkoff.travelapp.map.NoRouteMapActivity
import com.tinkoff.travelapp.menu.FAQActivity
import com.tinkoff.travelapp.menu.SettingsActivity
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.route.RouteItem
import kotlin.math.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loginPair: String

    private lateinit var adapter: TripCardAdapter
    private lateinit var tripPager: ViewPager2
    lateinit var viewModel: TripCardViewModel

    private val dbManager = DBManager(this)

    private var tripList = mutableListOf<TripDataModel>()
    private var tripNameBuffer: String = ""
    private var tripDateBuffer: String = ""
    private var tripDurationBuffer: TripDurations = TripDurations.SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginPair = intent.getStringExtra("loginPair").toString()

        adapter = TripCardAdapter()
        tripPager = findViewById(R.id.main_trip_pager)

        dbManager.openDb()
        getDbData()
        viewModel = ViewModelProvider(this)[TripCardViewModel::class.java]
        viewModel.clearLiveData()

        viewModel.tripDataList.observe(this) { list ->
            if (list.isSuccessful) {
                Log.d("Main", list.body().toString())
                tripPager.adapter = adapter
                list.body()?.let {
                    if (it.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.main_create_travel_popup_error_cant_create),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@observe
                    }
                    dbManager.writeTripDbData(
                        tripNameBuffer,
                        tripDateBuffer,
                        compressAndSortNewRoute(it, tripDurationBuffer),
                        loginPair
                    )
                    addToTripList(dbManager.readTripDbData(dbManager.getUserIdByLoginPair(loginPair)))
                    Toast.makeText(
                        this,
                        getString(R.string.main_create_travel_popup_travel_created),
                        Toast.LENGTH_SHORT
                    ).show()
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
        tripList.clear()
        tripList = dbManager.readTripDbData(dbManager.getUserIdByLoginPair(loginPair))
        adapter.setListOfRoutes(tripList)
        tripPager.adapter = adapter
    }

    private fun addToTripList(list: ArrayList<TripDataModel>) {
        tripList.clear()
        for (item in list) {
            tripList.add(item)
        }
        adapter.setListOfRoutes(tripList)
        tripPager.adapter = adapter
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
        dbManager.closeDb()
        super.onDestroy()
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
        tripDurationBuffer = tripDuration
        viewModel.getRoute(tripCategories, tripStartTime, tripEndTime, tripCost)
    }

    private fun compressAndSortNewRoute(route: Route, routeDuration: TripDurations): Route {
        val newRoute = Route()
        val random = Random(System.currentTimeMillis())

        val randomShortNumber: Int
        var index: Int
        when (routeDuration) {
            TripDurations.SHORT -> {
                randomShortNumber = random.nextInt(2, 3)
                for (i in 1..randomShortNumber) {
                    index = random.nextInt(route.size)
                    newRoute.add(route[index])
                    route.removeAt(index)
                    if (route.isEmpty()) {
                        break
                    }
                }
            }
            TripDurations.MEDIUM -> {
                randomShortNumber = random.nextInt(5, 6)
                for (i in 1..randomShortNumber) {
                    index = random.nextInt(route.size)
                    newRoute.add(route[index])
                    route.removeAt(index)
                    if (route.isEmpty()) {
                        break
                    }
                }
            }
            TripDurations.LONG -> {
                randomShortNumber = random.nextInt(9, 10)
                for (i in 1..randomShortNumber) {
                    index = random.nextInt(route.size)
                    newRoute.add(route[index])
                    route.removeAt(index)
                    if (route.isEmpty()) {
                        break
                    }
                }
            }
        }

        val firstPoint = newRoute.first()
        newRoute.sortWith(object : Comparator<RouteItem> {
            override fun compare(left: RouteItem?, right: RouteItem?): Int {
                return if (left != null && right != null) {
                    val distanceOne = distance(firstPoint, left)
                    val distanceTwo = distance(firstPoint, right)

                    (distanceOne - distanceTwo).toInt()
                } else {
                    -1
                }
            }

            fun distance(from: RouteItem, to: RouteItem): Double {
                val constEarthRadius = 6378137
                val deltaLat = from.coordinateX - to.coordinateX
                val deltaLon = from.coordinateY - to.coordinateY
                val angle = 2 * asin(
                    sqrt(
                        sin(deltaLat / 2).pow(2.0)
                                + cos(from.coordinateX) * cos(to.coordinateX) * sin(
                            deltaLon / 2
                        ).pow(2.0)
                    )
                )
                return constEarthRadius * angle
            }
        })

        return newRoute
    }
}
