package com.tinkoff.travelapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TripDescriptionActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_description)

        val itemImage: ImageView = findViewById(R.id.trip_description_image)
        val itemTitle: TextView = findViewById(R.id.trip_description_title)
        val itemDate: TextView = findViewById(R.id.trip_description_date)
        val itemKeyPoints: TextView = findViewById(R.id.trip_description_key_points)
        val itemTripDescription: TextView = findViewById(R.id.trip_description_text)

        val backButton: ImageButton = findViewById(R.id.trip_description_button_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        itemImage.setImageResource(intent.getIntExtra("itemImage", 0))
        itemTitle.text = intent.getStringExtra("itemTitle")
        itemDate.text = intent.getStringExtra("itemDate")
        itemKeyPoints.text = intent.getStringExtra("itemKeyPoints")
        itemTripDescription.text = intent.getStringExtra("itemTripDescription")
    }
}
