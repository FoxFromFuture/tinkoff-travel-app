package com.tinkoff.travelapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.tinkoff.travelapp.adapter.TripCardAdapter

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var imagesList = mutableListOf<Int>()
    private var titleList = mutableListOf<String>()
    private var dateList = mutableListOf<String>()
    private var keyPointsList = mutableListOf<String>()
    private var tripDescriptionList = mutableListOf<String>()
    private lateinit var adapter: TripCardAdapter
    private lateinit var trip_pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postToList()
        adapter = TripCardAdapter(imagesList, titleList, dateList, keyPointsList, tripDescriptionList)
        trip_pager = findViewById(R.id.trip_pager)
        trip_pager.adapter = adapter

        val buttonMenu = findViewById<ImageButton>(R.id.main_menu_button)
        buttonMenu.setOnClickListener(this)
    }

    private fun addToList(image: Int, title: String, date: String, key_points: String, trip_description: String) {
        imagesList.add(image)
        titleList.add(title)
        dateList.add(date)
        keyPointsList.add(key_points)
        tripDescriptionList.add(trip_description)
    }

    private fun postToList() {
        addToList(R.drawable.billy, "My first gachi trip", "04-29-2022", "Moscow - GachiCity", "Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text Ne text")
        addToList(R.drawable.billy_2, "My second gachi trip", "05-06-2022", "Moscow - Kazan", "Text")
        addToList(R.drawable.billy_3, "Big Boy Trip", "05-25-2022", "Moscow - New York", "Text")
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.main_menu_button -> {
                    val navigation = findViewById<DrawerLayout>(R.id.main_drawer_layout)
                    if (navigation.isDrawerOpen(GravityCompat.START)) {
                        navigation.closeDrawer(GravityCompat.START)
                    } else {
                        navigation.openDrawer(GravityCompat.START)
                    }
                }
            }
        }
    }
}
