package com.example.layoutmake.presentation.media.fragments

import android.app.ProgressDialog.show
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationRequestCompat.Quality
import androidx.navigation.fragment.findNavController
import com.example.layoutmake.R
import com.example.layoutmake.app.SHARED_PREFS
import com.example.layoutmake.databinding.FragmentNewPlaylistBinding
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.presentation.media.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var coverCount = 0
    private var coverSrc = "-1"
    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            showCancellingConfirmationDialog()
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

        sharedPrefs = requireContext().getSharedPreferences(SHARED_PREFS, 0)
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
            showCancellingConfirmationDialog()
        }

        binding.createPlaylistButton.setOnClickListener {
            createNewPlaylistAndNavigate()
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
        coverSrc = "cover_${coverCount}.jpg"
        val file = File(filePath, coverSrc)
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


    private fun showCancellingConfirmationDialog() {
        if (isFieldsEmpty()) {
            findNavController().navigateUp()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.cancelling_dialog_title)
                .setMessage(R.string.cancelling_dialog_message)
                .setNegativeButton(R.string.cancelling_dialog_negative) { _, _ -> }
                .setPositiveButton(R.string.cancelling_dialog_positive) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    private fun createNewPlaylistAndNavigate() {
        val newPlaylist = Playlist(
            playlistName = binding.editWorkoutName.text.toString(),
            playlistDescription = binding.editWorkoutDescription.text.toString(),
            coverSrc = coverSrc,
            tracksIds = emptyList(),
            tracksNumber = 0
        )
        viewModel.addNewPlaylist(newPlaylist)
        findNavController().navigateUp()
        showSnackbarCreated()
    }

    private fun isFieldsEmpty() =
        binding.editWorkoutName.text.isNullOrBlank() && binding.editWorkoutDescription.text.isNullOrBlank() && coverSrc == "-1"

    private fun showSnackbarCreated(){
        val snackbar = Snackbar.make(
            binding.newPlaylistConstraintLayout,
            getString(
                R.string.snackbar_new_playlist_message,
                binding.editWorkoutName.text.toString()
            ),
            Snackbar.LENGTH_SHORT
        )

        val view = snackbar.view
        val textView= view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.ellipsize = TextUtils.TruncateAt.END
        snackbar.show()
    }

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