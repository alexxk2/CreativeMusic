package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistTracksUseCase(private val mediaRepository: MediaRepository) {

    fun execute(listOfIds: List<Pair<Int, Long>>): Flow<List<Track>> = mediaRepository.getPlaylistTracks(listOfIds)
}