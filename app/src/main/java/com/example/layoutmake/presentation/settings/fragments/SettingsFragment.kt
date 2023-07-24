package com.example.layoutmake.presentation.settings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.layoutmake.R
import com.example.layoutmake.app.App
import com.example.layoutmake.databinding.FragmentSettingsBinding
import com.example.layoutmake.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSwitcher()

        with(binding)
        {


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
                (activity?.applicationContext as App).switchTheme(isChecked)
            }
        }
    }

    private fun setSwitcher(){
        val isDarkMode = viewModel.isDarkMode()
        binding.switchButton.isChecked = isDarkMode
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}