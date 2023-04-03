package com.example.layoutmake

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFS = "shared_prefs"
const val IS_DARK_THEME = "is_dark_theme"
const val HISTORY_LIST = "history_list"

class App: Application() {


    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(IS_DARK_THEME,false)

        if (darkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled

        val sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(IS_DARK_THEME,darkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }





}