package com.example.layoutmake.data.externals.settings.impl

import android.content.SharedPreferences
import com.example.layoutmake.IS_DARK_THEME
import com.example.layoutmake.data.externals.settings.LocalSettings

class LocalSettingsImpl(private val sharedPref: SharedPreferences) : LocalSettings {

    override fun isDarkMode(): Boolean = sharedPref.getBoolean(IS_DARK_THEME,false)

}