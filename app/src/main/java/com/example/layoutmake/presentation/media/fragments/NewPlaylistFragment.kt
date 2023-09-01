package com.example.layoutmake.presentation.media.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationRequestCompat.Quality
import androidx.navigation.fragment.findNavController
import com.example.layoutmake.app.SHARED_PREFS
import com.example.layoutmake.databinding.FragmentNewPlaylistBinding
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var coverCount = 0
    private val sharedPrefs = requireContext().getSharedPreferences(SHARED_PREFS, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coverCount = getCountFromSharedPrefs()
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            uri?.let {
                binding.addPlaylistImage.setImageURI(it)
                saveImageToPrivateStorage(it)
            }
        }

        binding.addPlaylistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.editWorkoutName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                manageCreateButtonAccess(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }


    }

    private fun manageCreateButtonAccess(input: CharSequence?) {
        binding.createPlaylistButton.isEnabled = !input.isNullOrBlank()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            COVERS
        )
        //create catalog if not exist
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "cover_${coverCount}.jpg")
        coverCount++
        putCountInSharedPrefs(coverCount)

        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outPutStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outPutStream)
    }

    private fun putCountInSharedPrefs(count: Int) {
        sharedPrefs.edit()
            .putInt(COVERS_COUNT, count)
            .apply()
    }

    private fun getCountFromSharedPrefs() = sharedPrefs.getInt(COVERS_COUNT, 0)

    override fun onResume() {
        super.onResume()
        manageCreateButtonAccess(binding.editWorkoutName.text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val COVERS = "covers"
        private const val COVERS_COUNT = "covers_count"
    }
}