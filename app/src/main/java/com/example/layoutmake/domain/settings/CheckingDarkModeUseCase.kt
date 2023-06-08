package com.example.layoutmake.domain.settings

import com.example.layoutmake.data.repositories.settings.SettingsRepository

class CheckingDarkModeUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(): Boolean = settingsRepository.isDarkMode()
}