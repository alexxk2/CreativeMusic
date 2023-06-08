package com.example.layoutmake.presentation.settings.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.layoutmake.App
import com.example.layoutmake.R
import com.example.layoutmake.SHARED_PREFS
import com.example.layoutmake.creator.Creator
import com.example.layoutmake.databinding.ActivitySettingsBinding
import com.example.layoutmake.presentation.settings.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        createViewModel()
        setSwitcher()

        with(binding)
        {
            arrowBackButton.setOnClickListener {
                finish()
            }

            shareTextView.setOnClickListener {
                viewModel.createShareIntent()
            }

            supportTextView.setOnClickListener {
                viewModel.createMessageIntent()
            }

            agreementTextView.setOnClickListener {
                viewModel.createBrowserIntent()
            }

            switchButton.setOnCheckedChangeListener { _, isChecked ->
                (applicationContext as App).switchTheme(isChecked)
            }
        }

    }

    private fun createViewModel(){
        val  sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val settingsRepository = Creator.getSettingsRepository(this,sharedPrefs)
        viewModel = ViewModelProvider(this,SettingsViewModel.getViewModelProvider(settingsRepository))[SettingsViewModel::class.java]
    }

    private fun setSwitcher(){
        val isDarkMode = viewModel.isDarkMode()
        binding.switchButton.isChecked = isDarkMode
    }

}

