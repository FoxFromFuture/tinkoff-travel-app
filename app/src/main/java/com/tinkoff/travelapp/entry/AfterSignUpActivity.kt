package com.tinkoff.travelapp.entry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.travelapp.MainActivity
import com.tinkoff.travelapp.R

class AfterSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_sign_up)

        val buttonLetsGo = findViewById<Button>(R.id.after_sign_up_lets_go)
        buttonLetsGo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
