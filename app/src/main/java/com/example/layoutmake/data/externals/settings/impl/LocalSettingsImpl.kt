package com.example.layoutmake.data.externals.settings.impl

import android.content.Context
import com.example.layoutmake.app.IS_DARK_THEME
import com.example.layoutmake.app.SHARED_PREFS
import com.example.layoutmake.data.externals.settings.LocalSettings

class LocalSettingsImpl(context: Context) : LocalSettings {

    private val sharedPref = context.getSharedPreferences(SHARED_PREFS,0)
    override fun isDarkMode(): Boolean = sharedPref.getBoolean(IS_DARK_THEME,false)

}