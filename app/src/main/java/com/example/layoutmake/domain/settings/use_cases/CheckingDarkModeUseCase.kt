package com.example.layoutmake.domain.settings.use_cases

import com.example.layoutmake.domain.repositories.SettingsRepository

class CheckingDarkModeUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(): Boolean = settingsRepository.isDarkMode()
}