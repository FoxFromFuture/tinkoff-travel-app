package com.tinkoff.travelapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMenu = findViewById<ImageButton>(R.id.main_menu_button)
        buttonMenu.setOnClickListener {
            val navigation = findViewById<DrawerLayout>(R.id.main_drawer_layout)
            if (navigation.isDrawerOpen(GravityCompat.START)) {
                navigation.closeDrawer(GravityCompat.START)
            } else {
                navigation.openDrawer(GravityCompat.START)
            }
        }
    }

    override fun onBackPressed() {
        return
    }
}
