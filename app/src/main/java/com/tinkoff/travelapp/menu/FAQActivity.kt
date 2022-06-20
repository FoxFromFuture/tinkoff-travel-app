package com.tinkoff.travelapp.menu

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.travelapp.R

class FAQActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_activity)

        val buttonBack = findViewById<ImageButton>(R.id.faq_back_button)
        buttonBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.faq_back_button -> {
                onBackPressed()
                finish()
            }
        }
    }
}
