package com.tinkoff.travelapp.application

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.tinkoff.travelapp.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory

class TinkoffTravelApp : Application() {
    override fun onCreate() {
        super.onCreate()

        when (PreferenceManager.getDefaultSharedPreferences(this)
            .getString("settings_theme_choosing", "")) {
            "SYNC" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setTheme(R.style.LightTheme_TravelApp)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setTheme(R.style.DarkTheme_TravelApp)
                    }
                }
            }
            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        MapKitFactory.setApiKey("7199a674-a718-43b5-9b0b-265e882bd690")
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
    }
}
