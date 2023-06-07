package com.example.layoutmake.domain.settings

import com.example.layoutmake.data.repositories.settings.SettingsRepository

class CreateMessageIntentUseCase(private val settingsRepository: SettingsRepository) {
    fun execute() = settingsRepository.createMessageIntent()
}