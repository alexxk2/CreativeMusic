package com.example.layoutmake.presentation.settings.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.layoutmake.data.repositories.settings.SettingsRepository
import com.example.layoutmake.domain.settings.CheckingDarkModeUseCase
import com.example.layoutmake.domain.settings.CreateBrowserIntentUseCase
import com.example.layoutmake.domain.settings.CreateMessageIntentUseCase
import com.example.layoutmake.domain.settings.CreateShareIntentUseCase

class SettingsViewModel(settingsRepository: SettingsRepository): ViewModel() {


    private val checkingDarkModeUseCase = CheckingDarkModeUseCase(settingsRepository)
    private val createBrowserIntentUseCase = CreateBrowserIntentUseCase(settingsRepository)
    private val createMessageIntentUseCase = CreateMessageIntentUseCase(settingsRepository)
    private val createShareIntentUseCase = CreateShareIntentUseCase(settingsRepository)


    fun isDarkMode()  = checkingDarkModeUseCase.execute()
    fun createBrowserIntent() = createBrowserIntentUseCase.execute()
    fun createMessageIntent() = createMessageIntentUseCase.execute()
    fun createShareIntent()  = createShareIntentUseCase.execute()

    companion object{
        fun getViewModelProvider(settingsRepository: SettingsRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(settingsRepository = settingsRepository)
            }
        }
    }
}