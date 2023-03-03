package com.example.layoutmake.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.models.Track

class TrackViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {

    private val albumImageView: ImageView = parentView.findViewById(R.id.album_image)
    private val songNameTextView: TextView = parentView.findViewById(R.id.song_name)
    private val albumAndTimeTextView: TextView = parentView.findViewById(R.id.artist_and_track_time)

    fun bind(track: Track) {

        Glide.with(albumImageView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(albumImageView)

        songNameTextView.text = track.trackName
        albumAndTimeTextView.text = parentView.context.getString(
            R.string.artist_and_time,
            track.artistName,
            track.trackTime
        )
    }
}

class TrackAdapter(
    private val dataSet: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val parentView =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size
}