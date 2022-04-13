package com.example.tinkoff_travel_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val buttonSignIn = findViewById<Button>(R.id.sign_in_sign_in_button)
        buttonSignIn.setOnClickListener {

        }

        val buttonForgotPassword = findViewById<TextView>(R.id.sign_in_forgot_password_text_button)
        buttonForgotPassword.setOnClickListener {

        }

        val buttonNotRegisteredYet = findViewById<TextView>(R.id.sign_in_not_registered_yet_text_button)
        buttonNotRegisteredYet.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}