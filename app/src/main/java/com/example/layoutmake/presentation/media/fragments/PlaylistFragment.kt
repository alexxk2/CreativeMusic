package com.example.layoutmake.presentation.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentPlaylistBinding
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.media.adapters.PlaylistTrackAdapter
import com.example.layoutmake.presentation.media.view_model.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var playlistId = 0
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var playlistTrackAdapter: PlaylistTrackAdapter
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getInt(PLAYLIST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        viewModel.getPlaylist(playlistId)

        viewModel.isListEmpty.observe(viewLifecycleOwner){isEmpty->
            if (isEmpty) showNoTracksToShareMessage()

        }

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            bindViews(playlist)
        }

        viewModel.playlistInfo.observe(viewLifecycleOwner) { playlistInfo ->
            bindViewPlaylistInfo(playlistInfo)
        }

        viewModel.listOfTracks.observe(viewLifecycleOwner) { listOfTracks ->
            playlistTrackAdapter.submitList(listOfTracks)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED

        }

        binding.emptySpace.doOnLayout {
            bottomSheetBehavior.setPeekHeight(binding.emptySpace.height, false)
        }

        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareButton.setOnClickListener {
            viewModel.sharePlaylistIfNotEmpty(playlistId)
        }

        binding.shareTextButton.setOnClickListener {
            viewModel.sharePlaylistIfNotEmpty(playlistId)
        }

        bottomSheetBehaviorMore = BottomSheetBehavior.from(binding.bottomSheetLayoutMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.moreButton.setOnClickListener {
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.deleteTextButton.setOnClickListener {
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
            showDeletePlaylistConfirmationDialog()
        }

        manageBottomSheetShadow()

    }

    private fun bindViews(playlist: Playlist) {
        with(binding) {

            playlist.coverSrc?.let {
                Glide.with(requireContext())
                    .load(it)
                    .centerCrop()
                    .into(playlistImage)
            }

            playlistNameTitle.text = playlist.playlistName
            playlistDescription.text = playlist.playlistDescription

            if (playlist.coverSrc == null) {
                includeFlatPlaylist.albumImage.setImageResource(R.drawable.placeholder_large)
            } else {
                Glide.with(includeFlatPlaylist.albumImage)
                    .load(playlist.coverSrc)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(includeFlatPlaylist.albumImage)
            }

            includeFlatPlaylist.playlistName.text = playlist.playlistName

        }
    }

    private fun bindViewPlaylistInfo(playlistInfo: Pair<String, String>) {
        binding.playlistInfo.text =
            getString(R.string.playlist_info, playlistInfo.first, playlistInfo.second)
        binding.includeFlatPlaylist.tracksNumber.text = playlistInfo.second
    }

    private fun setRecyclerView() {
        playlistTrackAdapter = PlaylistTrackAdapter(requireContext(), clickListener = { track ->
            val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlayerFragment(track)
            findNavController().navigate(action)

        }, longClickListener = { track ->
            showDeleteTrackConfirmationDialog(track)
        })

        binding.tracksRecyclerViewFlat.adapter = playlistTrackAdapter
        binding.tracksRecyclerViewFlat.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteTrackConfirmationDialog(track: Track) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_track_title)
            .setMessage("")
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track, playlistId)
            }
            .show()
    }

    private fun showDeletePlaylistConfirmationDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_delete_playlist_title,viewModel.playlist.value?.playlistName))
            .setMessage("")
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deletePlaylist(playlistId)
                findNavController().navigateUp()
            }
            .show()
    }

    private fun showNoTracksToShareMessage() {
        Snackbar.make(
            binding.playlistConstraintLayout,
            R.string.no_tracks_to_share,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun manageBottomSheetShadow() {
        bottomSheetBehaviorMore.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
    }

        companion object {
        const val PLAYLIST_ID = "playlistId"
    }
}