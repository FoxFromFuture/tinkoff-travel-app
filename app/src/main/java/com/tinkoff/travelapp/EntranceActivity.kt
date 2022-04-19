package com.tinkoff.travelapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class EntranceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        // Тут тоже студия вам говорит, что используется какой-то код, который
        // помечен как устаревший. Стоит использовать Handler(Looper.getMainLooper())
        Handler().postDelayed(
            {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            },
            1000
        )
    }
}
