package com.example.layoutmake.presentation.media.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.layoutmake.R
import com.example.layoutmake.databinding.PlaylistItemBinding
import com.example.layoutmake.domain.models.Playlist

class PlaylistsAdapter(
    private val clickListener: (playlist: Playlist) -> Unit
) : ListAdapter<Playlist, PlaylistsAdapter.PlaylistViewHolder>(DiffCallBack) {

    inner class PlaylistViewHolder(val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Playlist, clickListener: (playlist: Playlist) -> Unit) {

            with(binding) {

                root.setOnClickListener { clickListener(item) }
                playlistImage.setOnClickListener { clickListener(item) }

                if (item.coverSrc == null) {

                    playlistImage.setImageResource(R.drawable.placeholder_large)

                } else {

                    Glide.with(playlistImage)
                        .load(item.coverSrc)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(playlistImage)
                }

                playlistName.text = item.playlistName
                tracksNumber.text = getRightEndingTracks(item.tracksNumber)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemBinding.inflate(inflater, parent, false)

        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item, clickListener)

    }

    private fun getRightEndingTracks(numberOfTracks: Int): String {
        val preLastDigit = numberOfTracks % 100 / 10

        if (preLastDigit == 1) {
            return "$numberOfTracks треков"
        }

        return when (numberOfTracks % 10) {
            1 -> "$numberOfTracks трек"
            2 -> "$numberOfTracks трека"
            3 -> "$numberOfTracks трека"
            4 -> "$numberOfTracks трека"
            else -> "$numberOfTracks треков"
        }
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Playlist>() {

            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }
        }

    }
}