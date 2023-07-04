package com.example.layoutmake.domain.settings.use_cases

import com.example.layoutmake.data.repositories.settings.SettingsRepository

class CheckingDarkModeUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(): Boolean = settingsRepository.isDarkMode()
}