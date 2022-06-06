package com.tinkoff.travelapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val buttonSignIn = findViewById<Button>(R.id.sign_in_sign_in_button)
        buttonSignIn.setOnClickListener(this)

        val buttonForgotPassword = findViewById<TextView>(R.id.sign_in_forgot_password_text_button)
        buttonForgotPassword.setOnClickListener(this)

        val buttonNotRegisteredYet =
            findViewById<TextView>(R.id.sign_in_not_registered_yet_text_button)
        buttonNotRegisteredYet.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val context = this

        when (view?.id) {
            R.id.sign_in_sign_in_button -> {
                val intent = Intent(context, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
            R.id.sign_in_forgot_password_text_button -> {
                Toast.makeText(context, "I can't help you!", Toast.LENGTH_SHORT).show()
            }
            R.id.sign_in_not_registered_yet_text_button -> {
                val intent = Intent(context, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
