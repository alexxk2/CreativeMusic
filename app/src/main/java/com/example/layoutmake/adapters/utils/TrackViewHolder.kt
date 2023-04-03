package com.example.layoutmake.adapters.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.HISTORY_LIST
import com.example.layoutmake.R
import com.example.layoutmake.SHARED_PREFS
import com.example.layoutmake.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

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

        val convertedTime =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        albumAndTimeTextView.text = parentView.context.getString(
            R.string.artist_and_time,
            track.artistName,
            convertedTime
        )

        parentView.setOnClickListener {
            val sharedPref = parentView.context.getSharedPreferences(SHARED_PREFS, 0)
            val trackListJSon = sharedPref.getString(HISTORY_LIST, null)

            if (trackListJSon != null) {
                val trackList =
                    Gson().fromJson(trackListJSon, Array<Track>::class.java).toMutableList()

                if (trackList.contains(track)) {
                    trackList.remove(track)
                    trackList.add(0,track)
                } else if (trackList.size == 10) {
                    trackList.removeLast()
                    trackList.add(0,track)
                } else trackList.add(0,track)

                sharedPref.edit()
                    .putString(HISTORY_LIST,Gson().toJson(trackList))
                    .apply()

            } else {
                val defValue = Gson().toJson(arrayOf(track))
                sharedPref.edit()
                    .putString(HISTORY_LIST,defValue)
                    .apply()
            }

        }
    }
}