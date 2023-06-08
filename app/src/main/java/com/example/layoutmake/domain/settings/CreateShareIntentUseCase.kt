package com.example.layoutmake.domain.settings

import com.example.layoutmake.data.repositories.settings.SettingsRepository

class CreateShareIntentUseCase(private val settingsRepository: SettingsRepository) {
    fun execute() = settingsRepository.createShareIntent()
}