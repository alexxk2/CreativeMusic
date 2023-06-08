package com.example.layoutmake.data.repositories.settings

interface SettingsRepository {
    fun createBrowserIntent()
    fun createMessageIntent()
    fun createShareIntent()
    fun isDarkMode(): Boolean
}