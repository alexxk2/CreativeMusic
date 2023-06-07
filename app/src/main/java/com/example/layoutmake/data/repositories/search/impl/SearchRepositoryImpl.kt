package com.example.layoutmake.data.repositories.search.impl

import com.example.layoutmake.data.externals.search.HistoryManager
import com.example.layoutmake.data.externals.search.NetworkClient
import com.example.layoutmake.data.externals.search.dto.TrackRequestEntity
import com.example.layoutmake.data.externals.search.dto.TrackResponseEntity
import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val historyManager: HistoryManager,
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun getSearchHistory(): MutableList<Track> {
        return historyManager.getSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        return historyManager.addTrackToHistory(track)
    }

    override fun doesHistoryExist(): Boolean {
        return historyManager.doesHistoryExist()
    }

    override suspend fun startSearch(searchInput: String): List<Track> {
        val response = networkClient.startSearch(TrackRequestEntity(searchInput))

        if (response.resultCode == 200) {

            return (response as TrackResponseEntity).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }
        }
        else return emptyList()
    }

    override fun clearSearchHistory() {
        return historyManager.clearSearchHistory()
    }

    override fun isHistoryChanged(): Flow<Boolean> {
        return historyManager.isHistoryChanged()
    }

    override fun startHistoryListener() {
        historyManager.startHistoryListener()
    }
}