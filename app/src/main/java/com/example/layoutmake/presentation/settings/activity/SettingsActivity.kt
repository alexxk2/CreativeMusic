package com.example.layoutmake.presentation.settings.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.layoutmake.app.App
import com.example.layoutmake.R
import com.example.layoutmake.databinding.ActivitySettingsBinding
import com.example.layoutmake.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }

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

    private fun setSwitcher(){
        val isDarkMode = viewModel.isDarkMode()
        binding.switchButton.isChecked = isDarkMode
    }

}

