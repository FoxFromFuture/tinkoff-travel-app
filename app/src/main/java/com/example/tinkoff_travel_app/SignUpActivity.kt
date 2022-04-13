package com.example.tinkoff_travel_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val buttonBack = findViewById<ImageButton>(R.id.sign_up_back_button)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        val buttonSignIn = findViewById<Button>(R.id.sign_up_sign_up_button)
        buttonSignIn.setOnClickListener {
            val intent = Intent(this, afterSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
