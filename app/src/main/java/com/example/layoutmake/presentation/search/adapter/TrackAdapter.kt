package com.example.layoutmake.presentation.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.databinding.TrackItemBinding
import com.example.layoutmake.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter(
    private val context: Context,
    private var dataSet: MutableList<Track>,
    private val actionListener: TrackActionListener

) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(), View.OnClickListener {


    class TrackViewHolder(val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater,parent,false)
        binding.root.setOnClickListener(this)

        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = dataSet[position]
        val convertedTime =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())

        Glide.with(context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(holder.binding.albumImage)

        holder.binding.songName.text = item.trackName
        holder.binding.artistAndTrackTime.text = context.getString(R.string.artist_and_time,item.artistName,convertedTime)
        holder.binding.root.tag = item
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onClick(v: View?) {
        val track = v?.tag as Track
        actionListener.onClickTrack(track)
    }

    interface  TrackActionListener{
        fun onClickTrack(track: Track)
    }
}
