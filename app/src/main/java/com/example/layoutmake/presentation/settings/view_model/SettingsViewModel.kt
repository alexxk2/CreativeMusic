package com.example.layoutmake.presentation.settings.view_model


import androidx.lifecycle.ViewModel
import com.example.layoutmake.domain.settings.use_cases.CheckingDarkModeUseCase
import com.example.layoutmake.domain.settings.use_cases.CreateBrowserIntentUseCase
import com.example.layoutmake.domain.settings.use_cases.CreateMessageIntentUseCase
import com.example.layoutmake.domain.settings.use_cases.CreateShareIntentUseCase

class SettingsViewModel(
    private val checkingDarkModeUseCase: CheckingDarkModeUseCase,
    private val createBrowserIntentUseCase: CreateBrowserIntentUseCase,
    private val createMessageIntentUseCase: CreateMessageIntentUseCase,
    private val createShareIntentUseCase: CreateShareIntentUseCase
) : ViewModel() {


    fun isDarkMode() = checkingDarkModeUseCase.execute()
    fun createBrowserIntent() = createBrowserIntentUseCase.execute()
    fun createMessageIntent() = createMessageIntentUseCase.execute()
    fun createShareIntent() = createShareIntentUseCase.execute()


}