package com.tinkoff.travelapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.tinkoff.travelapp.adapter.TripCardAdapter
import com.tinkoff.travelapp.menu.AboutUsActivity
import com.tinkoff.travelapp.menu.FAQActivity
import com.tinkoff.travelapp.menu.MyAccountActivity
import com.tinkoff.travelapp.menu.SettingsActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var imagesList = mutableListOf<Int>()
    private var titleList = mutableListOf<String>()
    private var dateList = mutableListOf<String>()
    private var keyPointsList = mutableListOf<String>()
    private var tripDescriptionList = mutableListOf<String>()
    private lateinit var adapter: TripCardAdapter
    private lateinit var tripPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postToList()
        val viewModel= ViewModelProvider(this)[TripCardViewModel::class.java]

        viewModel.getStreet()
        viewModel.Street.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d("Main", response.body().toString())
                Log.d("Main", response.code().toString())
                Log.d("Main", response.headers().toString())
            } else {
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.Street.observe(this) { list ->
            list.body()?.let { adapter.setNextStreet(it) }
        }

        adapter =
            TripCardAdapter(imagesList, titleList, dateList, keyPointsList, tripDescriptionList)
        tripPager = findViewById(R.id.main_trip_pager)
        tripPager.adapter = adapter
//        viewModel.getRoute()
//        viewModel.tripDataList.observe(this) { list ->
//            list.body()?.let { adapter.setListOfRoutes(it) }
//        }
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
                R.id.main_menu_about_us_button -> {
                    val intent = Intent(this, AboutUsActivity::class.java)
                    startActivity(intent)
                }
                R.id.main_menu_logout_button -> {
                    val intent = Intent(this, AboutUsActivity::class.java)
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
    }

    private fun addToList(
        image: Int,
        title: String,
        date: String,
        key_points: String,
        trip_description: String
    ) {
        imagesList.add(image)
        titleList.add(title)
        dateList.add(date)
        keyPointsList.add(key_points)
        tripDescriptionList.add(trip_description)
    }

    private fun postToList() {
        addToList(
            R.drawable.base_trip_card_image,
            "Trip",
            "04-29-2022",
            "Moscow - City",
            "Text\n\n\nText"
        )
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
                val dialogBuilder = AlertDialog.Builder(this)
                val dialogView =
                    this.layoutInflater.inflate(R.layout.main_create_travel_popup, null)
                dialogBuilder.setView(dialogView)

                val alertDialog = dialogBuilder.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                alertDialog.show()
            }
            R.id.main_menu_logout_button -> {
                val intent = Intent(this, SignInActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
        }
    }
}
