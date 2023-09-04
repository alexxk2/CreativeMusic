package com.example.layoutmake.presentation.media.fragments


import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentNewPlaylistBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.media.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(TRACK, Track::class.java)
            } else {
                it.getParcelable(TRACK)
            }
            viewModel.getTrack(track)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            checksIfFieldsEmptyToShowCancellingDialogOrNot()
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

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            uri?.let {
                binding.addPlaylistImage.setImageURI(it)
                viewModel.saveImageToPrivateStorage(uri)
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
            checksIfFieldsEmptyToShowCancellingDialogOrNot()
        }

        binding.createPlaylistButton.setOnClickListener {
            createNewPlaylistAndNavigate()
        }
    }

    private fun manageCreateButtonAccess(input: CharSequence?) {
        binding.createPlaylistButton.isEnabled = !input.isNullOrBlank()
    }

    private fun checksIfFieldsEmptyToShowCancellingDialogOrNot() {
        if (isFieldsEmpty()) {
            chooseBackNavigationAndNavigate()
        } else {
            showCancellingConfirmationDialog()
        }
    }

    private fun showCancellingConfirmationDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.cancelling_dialog_title)
            .setMessage(R.string.cancelling_dialog_message)
            .setNegativeButton(R.string.cancelling_dialog_negative) { _, _ -> }
            .setPositiveButton(R.string.cancelling_dialog_positive) { _, _ ->
                chooseBackNavigationAndNavigate()
            }
            .show()
    }

    private fun createNewPlaylistAndNavigate() {
        viewModel.addNewPlaylist(
            playlistName = binding.editWorkoutName.text,
            playlistDescription = binding.editWorkoutDescription.text
        )
        chooseBackNavigationAndNavigate()
        showSnackbarCreated()
    }

    private fun isFieldsEmpty() =
        binding.editWorkoutName.text.isNullOrBlank() && binding.editWorkoutDescription.text.isNullOrBlank() && viewModel.coverSrc.value == null

    private fun showSnackbarCreated() {
        val snackbar = Snackbar.make(
            binding.newPlaylistConstraintLayout,
            getString(
                R.string.snackbar_new_playlist_message,
                binding.editWorkoutName.text.toString()
            ),
            Snackbar.LENGTH_SHORT
        )

        val view = snackbar.view
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.ellipsize = TextUtils.TruncateAt.END
        snackbar.show()
    }

    private fun chooseBackNavigationAndNavigate() {
        if (viewModel.track.value != null) {
            val action =
                NewPlaylistFragmentDirections.actionNewPlaylistFragmentToPlayerFragment(viewModel.track.value!!)
            findNavController().navigate(action)
        } else {
            findNavController().navigateUp()
        }
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
        private const val TRACK = "track"
    }
}