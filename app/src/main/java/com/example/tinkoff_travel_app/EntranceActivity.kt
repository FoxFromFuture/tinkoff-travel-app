package com.example.tinkoff_travel_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class EntranceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        Handler().postDelayed(
            {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            },
            1000
        )
    }
}
