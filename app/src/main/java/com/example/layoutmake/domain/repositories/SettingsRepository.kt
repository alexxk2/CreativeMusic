package com.example.layoutmake.domain.repositories

interface SettingsRepository {
    fun createBrowserIntent()
    fun createMessageIntent()
    fun createShareIntent()
    fun isDarkMode(): Boolean
}