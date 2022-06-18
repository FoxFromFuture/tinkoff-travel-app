package com.tinkoff.travelapp.menu

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tinkoff.travelapp.HideItFragment
import com.tinkoff.travelapp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val buttonAbout = findPreference<Preference>("settings_about")
        if (buttonAbout != null) {
            buttonAbout.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent(activity, AboutUsActivity::class.java)
                startActivity(intent)
                true
            }
        }

        val buttonLanguage = findPreference<Preference>("settings_language_choosing")
        if (buttonLanguage != null) {
            buttonLanguage.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.settings_activity, HideItFragment(), HideItFragment.TAG)
                    ?.addToBackStack(null)
                    ?.commit()
                true
            }
        }

        val listTheme = findPreference<ListPreference>("settings_theme_choosing")
        listTheme?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    "SYNC" -> {
                        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                            Configuration.UI_MODE_NIGHT_YES -> {
                                activity?.setTheme(R.style.LightTheme_TravelApp)
                            }
                            Configuration.UI_MODE_NIGHT_NO -> {
                                activity?.setTheme(R.style.DarkTheme_TravelApp)
                            }
                        }
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    "LIGHT" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "DARK" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
                true
            }
    }
}
