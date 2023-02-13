package com.example.layoutmake
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.layoutmake.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.arrowBackButton.setOnClickListener{
            finish()
        }
    }
}