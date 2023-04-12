package com.example.layoutmake

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.databinding.ActivityPlayerBinding
import com.example.layoutmake.models.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater).also { setContentView(it.root) }


        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track.DEFAULT
        } else intent.getParcelableExtra(TRACK) ?: Track.DEFAULT

        inflateViews()

        binding.arrowBackButton.setOnClickListener { finish() }
    }

    private fun inflateViews() {
        with(binding) {

            val convertedTime =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis.toLong())

            Glide.with(this@PlayerActivity)
                .load(getCoverArtwork(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(16))
                .into(cover)

            songName.text = track.trackName
            artistName.text = track.artistName
            trackDurationValue.text = convertedTime

            if (track.collectionName != null) {
                albumNameValue.text = track.collectionName
            } else {
                albumName.isVisible = false
                albumNameValue.isVisible = false
            }

            trackYearValue.text = track.releaseDate?.take(4)
            trackGenreValue.text = track.primaryGenreName
            trackCountryValue.text = track.country
        }

    }

    private fun getCoverArtwork(oldUrl: String) = oldUrl.replaceAfterLast('/', "512x512bb.jpg")

    companion object {
        const val TRACK = "track"
    }


}