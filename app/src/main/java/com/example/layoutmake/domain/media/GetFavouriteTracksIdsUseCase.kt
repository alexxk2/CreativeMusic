package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteTracksIdsUseCase(private val mediaRepository: MediaRepository) {

    fun execute(): Flow<List<Int>> = mediaRepository.getFavouriteTracksIds()
}