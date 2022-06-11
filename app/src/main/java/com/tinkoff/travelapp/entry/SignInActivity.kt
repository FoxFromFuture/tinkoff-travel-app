package com.tinkoff.travelapp.entry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.travelapp.AuthorizationViewModel
import com.tinkoff.travelapp.MainActivity
import com.tinkoff.travelapp.R
import java.util.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        viewModel = ViewModelProvider(this)[AuthorizationViewModel::class.java]
        viewModel.street.observe(this) { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, getString(R.string.sign_in_welcome_back), Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.sign_in_invalid_email_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

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
                val authEmail = findViewById<EditText>(R.id.sign_in_email_input).text.toString()
                val authPassword =
                    findViewById<EditText>(R.id.sign_in_password_input).text.toString()
                val encoder = Base64.getEncoder()
                viewModel.getStreet(
                    "Basic " + encoder.encodeToString("${authEmail}:${authPassword}".toByteArray())
                )
            }
            R.id.sign_in_forgot_password_text_button -> {
                Toast.makeText(context, getString(R.string.sign_in_cant), Toast.LENGTH_SHORT).show()
            }
            R.id.sign_in_not_registered_yet_text_button -> {
                val intent = Intent(context, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
