package com.example.layoutmake.presentation.player.adapters

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.layoutmake.R
import com.example.layoutmake.databinding.PlaylistItemFlatBinding
import com.example.layoutmake.domain.models.Playlist
import java.io.File

class PlaylistsFlatAdapter(
    private val context: Context,
    private val clickListener: (playlist: Playlist) -> Unit
) : ListAdapter<Playlist, PlaylistsFlatAdapter.PlaylistViewHolder>(DiffCallBack) {

    inner class PlaylistViewHolder(val binding: PlaylistItemFlatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Playlist, clickListener: (playlist: Playlist) -> Unit) {
            with(binding) {

                binding.root.setOnClickListener { clickListener(item) }

                if (item.coverSrc == null) {

                    albumImage.setImageResource(R.drawable.placeholder)
                    albumImage.scaleType = ImageView.ScaleType.CENTER

                } else {
                    val filePath =
                        File(
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            COVERS
                        )
                    val file = File(filePath, item.coverSrc)
                    val coverUri = file.toUri()

                    Glide.with(albumImage)
                        .load(coverUri)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(albumImage)
                }

                playlistName.text = item.playlistName
                tracksNumber.text = item.tracksNumber.toString()
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemFlatBinding.inflate(inflater, parent, false)

        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        val item = getItem(position)

        holder.bind(item, clickListener)
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