package com.example.layoutmake.data.repositories.settings.impl

import com.example.layoutmake.data.externals.settings.ExternalNavigator
import com.example.layoutmake.data.externals.settings.LocalSettings
import com.example.layoutmake.data.repositories.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    private val localSettings: LocalSettings
) : SettingsRepository {

    override fun createBrowserIntent() {
        externalNavigator.createBrowserIntent()
    }

    override fun createMessageIntent() {
        externalNavigator.createMessageIntent()
    }

    override fun createShareIntent() {
        externalNavigator.createShareIntent()
    }

    override fun isDarkMode(): Boolean  = localSettings.isDarkMode()
}