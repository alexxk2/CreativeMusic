package com.example.layoutmake.presentation.media.adapters

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.layoutmake.R
import com.example.layoutmake.databinding.PlaylistItemBinding
import com.example.layoutmake.domain.models.Playlist
import java.io.File

class PlaylistsAdapter(
    private val context: Context,
    private val actionListener: PlaylistClickListener
) : ListAdapter<Playlist, PlaylistsAdapter.PlaylistViewHolder>(DiffCallBack),
    View.OnClickListener {

    class PlaylistViewHolder(val binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.playlistImageCardItem.setOnClickListener(this)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistsAdapter.PlaylistViewHolder, position: Int) {

        val item = getItem(position)



        with(holder.binding) {
            if (item.coverSrc == "-1") {

                playlistImage.setImageResource(R.drawable.placeholder_large)

            } else {
                val filePath =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), COVERS)
                val file = File(filePath, item.coverSrc)
                val coverUri = file.toUri()

                Glide.with(playlistImage)
                    .load(coverUri)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(playlistImage)
            }

            playlistName.text = item.playlistName
            tracksNumber.text = item.tracksNumber.toString()

            root.tag = item
            playlistImageCardItem.tag = item
        }
    }

    override fun onClick(v: View?) {
        val playlist = v?.tag as Playlist
        when (v.id) {
            R.id.playlist_image_card_item -> actionListener.onPlaylistClick(playlist)
            else -> actionListener.onPlaylistClick(playlist)
        }
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
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

        private const val COVERS = "covers"
    }
}