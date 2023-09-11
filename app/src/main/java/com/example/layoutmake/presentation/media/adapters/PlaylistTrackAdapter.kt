package com.example.layoutmake.presentation.media.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.databinding.TrackItemBinding
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class PlaylistTrackAdapter(
    private val context: Context,
    private val clickListener: (item: Track) -> Unit,
    private val longClickListener: (item: Track) -> Unit,
) : ListAdapter<Track, PlaylistTrackAdapter.TrackViewHolder>(DiffCallBack) {


    inner class TrackViewHolder(val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Track,
            clickListener: (item: Track) -> Unit,
            longClickListener: (item: Track) -> Unit
        ) {

            binding.root.setOnClickListener { clickListener(item) }
            binding.root.setOnLongClickListener {
                longClickListener(item)
                true
            }

            val convertedTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())

            Glide.with(context)
                .load(item.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(4))
                .into(binding.albumImage)

            binding.songName.text = item.trackName
            binding.artistAndTrackTime.text =
                context.getString(R.string.artist_and_time, item.artistName, convertedTime)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater, parent, false)

        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, longClickListener)
    }


    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Track>() {

            override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem == newItem
            }
        }

    }
}
