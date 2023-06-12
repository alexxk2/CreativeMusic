package com.example.layoutmake.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.player.use_cases.GetPlayerStateUseCase
import com.example.layoutmake.domain.player.use_cases.GetTrackCurrentPositionUseCase
import com.example.layoutmake.domain.player.use_cases.PauseSongUseCase
import com.example.layoutmake.domain.player.use_cases.PlaySongUseCase
import com.example.layoutmake.domain.player.use_cases.PreparePlayerUseCase
import com.example.layoutmake.domain.player.use_cases.ReleasePlayerUseCase
import com.example.layoutmake.presentation.player.model.PlayerState
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val getTrackCurrentPositionUseCase: GetTrackCurrentPositionUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val preparePlayerUseCase: PreparePlayerUseCase,
    private val releasePlayerUseCase: ReleasePlayerUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase

) : ViewModel() {


    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Draw(track))
    val playerState: LiveData<PlayerState> = _playerState

    init {
        viewModelScope.launch {
            getPlayerStateUseCase.execute().collect {playerState->
                when (playerState) {
                    PLAYER_STATE_LOADING -> _playerState.postValue(PlayerState.Loading)
                    PLAYER_STATE_PREPARED -> _playerState.postValue(PlayerState.Prepared)
                    PLAYER_STATE_PLAYING -> _playerState.postValue(PlayerState.Playing)
                    PLAYER_STATE_COMPLETED -> _playerState.postValue(PlayerState.Completed)
                    PLAYER_STATE_PAUSED -> _playerState.postValue(PlayerState.Paused)
                }
            }
        }
    }

    fun playSong() {
        playSongUseCase.execute()
    }

    fun pauseSong() {
        pauseSongUseCase.execute()
    }

    fun preparePlayer() {
        track.previewUrl?.let { preparePlayerUseCase.execute(it) }
    }

    fun getTrackCurrentPosition() = getTrackCurrentPositionUseCase.execute()


    fun releasePlayer() {
        releasePlayerUseCase.execute()
    }

    companion object {

        const val PLAYER_STATE_LOADING = 1
        const val PLAYER_STATE_PREPARED = 2
        const val PLAYER_STATE_PLAYING = 3
        const val PLAYER_STATE_COMPLETED = 4
        const val PLAYER_STATE_PAUSED = 5
    }
}

