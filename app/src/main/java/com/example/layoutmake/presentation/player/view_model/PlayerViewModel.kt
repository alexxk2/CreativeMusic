package com.example.layoutmake.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.layoutmake.data.repositories.player.PlayerRepository
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.player.use_cases.GetPlayerStateUseCase
import com.example.layoutmake.domain.player.use_cases.GetTrackCurrentPositionUseCase
import com.example.layoutmake.domain.player.use_cases.PauseSongUseCase
import com.example.layoutmake.domain.player.use_cases.PlaySongUseCase
import com.example.layoutmake.domain.player.use_cases.PreparePlayerUseCase
import com.example.layoutmake.domain.player.use_cases.ReleasePlayerUseCase
import com.example.layoutmake.domain.search.StartHistoryListenerUseCase
import com.example.layoutmake.presentation.player.model.PlayerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val getTrackCurrentPositionUseCase = GetTrackCurrentPositionUseCase(playerRepository)
    private val pauseSongUseCase = PauseSongUseCase(playerRepository)
    private val playSongUseCase = PlaySongUseCase(playerRepository)
    private val preparePlayerUseCase = PreparePlayerUseCase(playerRepository)
    private val releasePlayerUseCase = ReleasePlayerUseCase(playerRepository)
    private val getPlayerStateUseCase = GetPlayerStateUseCase(playerRepository)


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
        fun getViewModelFactory(
            track: Track,
            playerRepository: PlayerRepository
        ): ViewModelProvider.Factory = viewModelFactory {

            initializer {
                PlayerViewModel(
                    track = track,
                    playerRepository = playerRepository
                )
            }
        }

        const val PLAYER_STATE_LOADING = 1
        const val PLAYER_STATE_PREPARED = 2
        const val PLAYER_STATE_PLAYING = 3
        const val PLAYER_STATE_COMPLETED = 4
        const val PLAYER_STATE_PAUSED = 5
    }
}

