package com.tinkoff.travelapp.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.BuildConfig
import com.tinkoff.travelapp.R

class AboutUsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val textVersion = findViewById<TextView>(R.id.settings_about_version)
        textVersion.text =
            String.format(getString(R.string.settings_about_version_text), BuildConfig.VERSION_NAME)

        val buttonBack = findViewById<ImageButton>(R.id.settings_about_back_button)
        buttonBack.setOnClickListener(this)

        val buttonGithub = findViewById<TextView>(R.id.settings_about_link)
        buttonGithub.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.settings_about_back_button -> {
                onBackPressed()
                finish()
            }
            R.id.settings_about_link -> {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.settings_about_link)))
                startActivity(intent)
            }
        }
    }
}
