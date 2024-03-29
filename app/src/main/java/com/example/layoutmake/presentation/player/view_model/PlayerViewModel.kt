package com.example.layoutmake.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.AddTrackToFavouriteUseCase
import com.example.layoutmake.domain.media.AddTrackToSavedUseCase
import com.example.layoutmake.domain.media.CheckIfPlaylistContainsTrackUseCase
import com.example.layoutmake.domain.media.GetAllPlaylistsUseCase
import com.example.layoutmake.domain.media.RemoveTrackFromFavouriteUseCase
import com.example.layoutmake.domain.media.UpdatePlaylistUseCase
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.player.use_cases.GetPlayerStateUseCase
import com.example.layoutmake.domain.player.use_cases.GetTrackCurrentPositionUseCase
import com.example.layoutmake.domain.player.use_cases.PauseSongUseCase
import com.example.layoutmake.domain.player.use_cases.PlaySongUseCase
import com.example.layoutmake.domain.player.use_cases.PreparePlayerUseCase
import com.example.layoutmake.domain.player.use_cases.ReleasePlayerUseCase
import com.example.layoutmake.presentation.player.model.AddedState
import com.example.layoutmake.presentation.player.model.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PlayerViewModel(
    private val track: Track,
    private val getTrackCurrentPositionUseCase: GetTrackCurrentPositionUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val preparePlayerUseCase: PreparePlayerUseCase,
    private val releasePlayerUseCase: ReleasePlayerUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
    private val addTrackToFavouriteUseCase: AddTrackToFavouriteUseCase,
    private val removeTrackFromFavouriteUseCase: RemoveTrackFromFavouriteUseCase,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val addTrackToSavedUseCase: AddTrackToSavedUseCase,
    private val checkIfPlaylistContainsTrackUseCase: CheckIfPlaylistContainsTrackUseCase

) : ViewModel() {

    private var timerJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Draw(track))
    val playerState: LiveData<PlayerState> = _playerState

    private val _playerTime = MutableLiveData<String>("00:00")
    val playerTime: LiveData<String> = _playerTime

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _addedState = MutableLiveData<AddedState>()
    val addedState: LiveData<AddedState> = _addedState

    init {
        viewModelScope.launch {
            getPlayerStateUseCase.execute().collect { playerState ->
                when (playerState) {
                    PLAYER_STATE_LOADING -> _playerState.postValue(PlayerState.Loading)
                    PLAYER_STATE_PREPARED -> _playerState.postValue(PlayerState.Prepared)
                    PLAYER_STATE_PLAYING -> _playerState.postValue(PlayerState.Playing)
                    PLAYER_STATE_COMPLETED -> _playerState.postValue(PlayerState.Completed)
                    PLAYER_STATE_PAUSED -> _playerState.postValue(PlayerState.Paused)
                }
            }
        }
        _isFavourite.value = track.isFavourite
    }

    private fun addTrackToSaved(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            addTrackToSavedUseCase.execute(track)
        }
    }

    private fun updatePlaylistAndGetUpdatedOne(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            updatePlaylistUseCase.execute(track, playlist)
            getAllPlaylists()
        }
    }

    fun addTrackToExactPlaylist(track: Track, playlist: Playlist) {

        if (checkIfPlaylistContainsTrackUseCase.execute(track = track, playList = playlist)) {
            _addedState.value = AddedState.NotDone(playlist)

        } else {
            addTrackToSaved(track)
            updatePlaylistAndGetUpdatedOne(track,playlist)
            _addedState.value = AddedState.Done(playlist)
        }
        _addedState.value = AddedState.Ready
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            getAllPlaylistsUseCase.execute().collect { newList ->
                _playlists.value = newList
            }
        }
    }

    fun addTrackToFavourite(track: Track) {
        _isFavourite.value = true

        viewModelScope.launch {
            addTrackToFavouriteUseCase.execute(track)
        }
    }

    fun removeTrackFromFavourite(track: Track) {
        _isFavourite.value = false

        viewModelScope.launch {
            removeTrackFromFavouriteUseCase.execute(track)
        }
    }

    fun playSong() {
        playSongUseCase.execute()
        startTimer()
    }

    fun pauseSong() {
        pauseSongUseCase.execute()
        timerJob?.cancel()
    }

    fun preparePlayer() {
        track.previewUrl?.let { preparePlayerUseCase.execute(it) }
    }

    fun getTrackCurrentPosition() = getTrackCurrentPositionUseCase.execute()


    fun releasePlayer() {
        releasePlayerUseCase.execute()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            delay(DELAY_BEFORE_STATE_CHANGED)
            while (_playerState.value == PlayerState.Playing) {
                delay(TIMER_UPDATE_INTERVAL_MS)
                _playerTime.value = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(getTrackCurrentPositionUseCase.execute())
                if (_playerState.value == PlayerState.Completed) {
                    _playerTime.value = "00:00"
                    timerJob?.cancel()
                }
            }
        }
    }

    companion object {
        const val PLAYER_STATE_LOADING = 1
        const val PLAYER_STATE_PREPARED = 2
        const val PLAYER_STATE_PLAYING = 3
        const val PLAYER_STATE_COMPLETED = 4
        const val PLAYER_STATE_PAUSED = 5
        private const val TIMER_UPDATE_INTERVAL_MS = 300L
        private const val DELAY_BEFORE_STATE_CHANGED = 100L
    }
}

