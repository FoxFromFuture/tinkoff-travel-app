package com.tinkoff.travelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class afterSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_sign_up)
    }

    override fun onBackPressed() {
        return
    }
}
