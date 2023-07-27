package com.example.layoutmake.domain.settings.use_cases

import com.example.layoutmake.domain.repositories.SettingsRepository

class CreateBrowserIntentUseCase(private val settingsRepository: SettingsRepository) {
    fun execute() = settingsRepository.createBrowserIntent()
}