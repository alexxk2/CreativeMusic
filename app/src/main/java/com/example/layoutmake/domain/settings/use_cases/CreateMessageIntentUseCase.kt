package com.example.layoutmake.domain.settings.use_cases

import com.example.layoutmake.domain.repositories.SettingsRepository

class CreateMessageIntentUseCase(private val settingsRepository: SettingsRepository) {
    fun execute() = settingsRepository.createMessageIntent()
}