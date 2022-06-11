package com.tinkoff.travelapp.entry

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.travelapp.R

class EntranceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000
        )
    }
}
