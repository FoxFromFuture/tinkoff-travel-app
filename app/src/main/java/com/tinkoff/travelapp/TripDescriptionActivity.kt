package com.tinkoff.travelapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.travelapp.map.RouteMapActivity

class TripDescriptionActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_description)

        val itemImage: ImageView = findViewById(R.id.trip_description_image)
        val itemTitle: TextView = findViewById(R.id.trip_description_title)
        val itemDate: TextView = findViewById(R.id.trip_description_date)
        val itemKeyPoints: TextView = findViewById(R.id.trip_description_key_points)
        val itemTripDescription: TextView = findViewById(R.id.trip_description_text)

        val buttonBack = findViewById<ImageButton>(R.id.trip_description_button_back)
        buttonBack.setOnClickListener(this)

        val buttonMap = findViewById<ImageButton>(R.id.trip_description_button_map)
        buttonMap.setOnClickListener(this)

        itemImage.setImageResource(intent.getIntExtra("itemImage", 0))
        itemTitle.text = intent.getStringExtra("itemTitle")
        itemDate.text = intent.getStringExtra("itemDate")
        itemKeyPoints.text = intent.getStringExtra("itemKeyPoints")
        itemTripDescription.text = intent.getStringExtra("itemTripDescription")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.trip_description_button_map -> {
                val itemRealRoute = intent.getStringExtra("itemRealRoute")
                val intent = Intent(this, RouteMapActivity::class.java)

                intent.putExtra("itemRealRoute", itemRealRoute)

                startActivity(intent)
            }
            R.id.trip_description_button_back -> {
                onBackPressed()
            }
        }
    }
}
