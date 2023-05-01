package com.example.layoutmake.adapters.utils

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.PlayerActivity
import com.example.layoutmake.R
import com.example.layoutmake.SHARED_PREFS
import com.example.layoutmake.models.SearchHistory
import com.example.layoutmake.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {

    private val albumImageView: ImageView = parentView.findViewById(R.id.album_image)
    private val songNameTextView: TextView = parentView.findViewById(R.id.song_name)
    private val albumAndTimeTextView: TextView = parentView.findViewById(R.id.artist_and_track_time)
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

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
            if (clickDebounce()){
                val sharedPref = parentView.context.getSharedPreferences(SHARED_PREFS, 0)

                val searchHistory = SearchHistory(sharedPref)
                searchHistory.manageTrackHistory(track)

                val playerIntent = Intent(parentView.context, PlayerActivity::class.java)
                playerIntent.putExtra(TRACK, track)
                parentView.context.startActivity(playerIntent)
            }
        }
    }

    private fun clickDebounce (): Boolean{
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed=true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object{
        private const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}