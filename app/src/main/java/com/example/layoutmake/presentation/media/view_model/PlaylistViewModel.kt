package com.example.layoutmake.presentation.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.DeletePlaylistUseCase
import com.example.layoutmake.domain.media.DeleteTrackFromPlaylistUseCase
import com.example.layoutmake.domain.media.GetPlaylistTracksUseCase
import com.example.layoutmake.domain.media.GetPlaylistUseCase
import com.example.layoutmake.domain.media.SharePlaylistUseCase
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PlaylistViewModel(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase

) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _listOfTracks = MutableLiveData<List<Track>>()
    val listOfTracks: LiveData<List<Track>> = _listOfTracks

    private val _playlistInfo = MutableLiveData<Pair<String, String>>()
    val playlistInfo: LiveData<Pair<String, String>> = _playlistInfo

    private val _isListEmpty = MutableLiveData<Boolean>()
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    private val _showNoTracksMessage = MutableLiveData<Boolean>()
    val showNoTracksMessage: LiveData<Boolean> = _showNoTracksMessage


    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlaylistUseCase.execute(playlistId)
        }
    }

    fun sharePlaylistIfNotEmpty(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isListEmpty.value == true) {
                _showNoTracksMessage.postValue(true)
                delay(CHECK_RETURN_STATE_DELAY)
                _showNoTracksMessage.postValue(false)
            } else {
                sharePlaylistUseCase.execute(playlistId)
            }
        }

    }

    fun deleteTrackFromPlaylist(track: Track, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTrackFromPlaylistUseCase.execute(track, playlistId)
            getPlaylist(playlistId)
        }
    }

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistUseCase.execute(playlistId).collect { givenPlaylist ->
                _playlist.postValue(givenPlaylist)
                getPlaylistTracks(givenPlaylist.tracksIds)
            }
        }
    }

    private fun getPlaylistTracks(listOfIds: List<Pair<Int, Long>>) {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistTracksUseCase.execute(listOfIds).collect { currentList ->
                _listOfTracks.postValue(currentList)
                calculatePlaylistDuration(currentList)
                _isListEmpty.postValue(currentList.isEmpty())
            }
        }
    }

    private fun calculatePlaylistDuration(listOfTracks: List<Track>) {

        var totalTimeMls = 0
        listOfTracks.forEach { totalTimeMls += it.trackTimeMillis.toInt() }
        val totalMinutes = SimpleDateFormat("mm", Locale.getDefault()).format(totalTimeMls).toInt()
        _playlistInfo.postValue(
            Pair(
                getRightEndingMinutes(totalMinutes),
                getRightEndingTracks(listOfTracks.size)
            )
        )
    }

    private fun getRightEndingTracks(numberOfTracks: Int): String {
        val preLastDigit = numberOfTracks % 100 / 10

        if (preLastDigit == 1) {
            return "$numberOfTracks треков"
        }

        return when (numberOfTracks % 10) {
            1 -> "$numberOfTracks трек"
            2 -> "$numberOfTracks трека"
            3 -> "$numberOfTracks трека"
            4 -> "$numberOfTracks трека"
            else -> "$numberOfTracks треков"
        }
    }

    private fun getRightEndingMinutes(minutes: Int): String {
        val preLastDigit = minutes % 100 / 10

        if (preLastDigit == 1) {
            return "$minutes минут"
        }

        return when (minutes % 10) {
            1 -> "$minutes минута"
            2 -> "$minutes минуты"
            3 -> "$minutes минуты"
            4 -> "$minutes минуты"
            else -> "$minutes минут"
        }
    }

    companion object{
        const val CHECK_RETURN_STATE_DELAY = 300L
    }

}