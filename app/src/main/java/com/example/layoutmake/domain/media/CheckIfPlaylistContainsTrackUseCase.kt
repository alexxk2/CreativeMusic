package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track

class CheckIfPlaylistContainsTrackUseCase {

    fun execute(track: Track, playList: Playlist) = playList.tracksIds.contains(track.trackId)
}