package com.example.githubusersearch.ui.view

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubusersearch.R
import com.example.githubusersearch.SettingPreferences
import com.example.githubusersearch.dataStore
import com.example.githubusersearch.ui.viewmodel.SettingViewModelFactory
import com.example.githubusersearch.ui.viewmodel.SwitchThemeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SwitchThemeActivity : AppCompatActivity() {
    private val switchThemeViewModel by viewModels<SwitchThemeViewModel> {
        SettingViewModelFactory(SettingPreferences.getInstance(application.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_theme)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        switchThemeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            switchThemeViewModel.saveThemeSetting(isChecked)
        }
    }
}