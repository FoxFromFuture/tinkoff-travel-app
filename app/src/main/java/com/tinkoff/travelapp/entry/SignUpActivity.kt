package com.tinkoff.travelapp.entry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.travelapp.R

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val buttonBack = findViewById<ImageButton>(R.id.sign_up_back_button)
        buttonBack.setOnClickListener(this)

        val buttonSignIn = findViewById<Button>(R.id.sign_up_sign_up_button)
        buttonSignIn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.sign_up_back_button -> {
                onBackPressed()
            }
            R.id.sign_up_sign_up_button -> {
                val intent = Intent(this, AfterSignUpActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }
}
