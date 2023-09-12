package com.example.layoutmake.presentation.media.view_model

import android.net.Uri
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.AddNewPlaylistUseCase
import com.example.layoutmake.domain.media.GetPlaylistUseCase
import com.example.layoutmake.domain.media.SaveImageUseCase
import com.example.layoutmake.domain.media.UpdatePlaylistUseCase
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.media.model.NewPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val addNewPlaylistUseCase: AddNewPlaylistUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase
) : ViewModel() {

    private val _track = MutableLiveData<Track?>(null)
    val track: LiveData<Track?> = _track

    private val _coverSrc = MutableLiveData<Uri?>(null)
    val coverSrc: LiveData<Uri?> = _coverSrc

    private val _screenState = MutableLiveData<NewPlaylistState>()
    val screenState: LiveData<NewPlaylistState> = _screenState

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _coverSrc.postValue(saveImageUseCase.execute(uri))
        }
    }

    fun getTrack(track: Track?) {
        _track.value = track
    }

    fun addNewPlaylist(playlistName: Editable?, playlistDescription: Editable?) {
        viewModelScope.launch {
            addNewPlaylistUseCase.execute(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                uri = _coverSrc.value
            )
        }
    }

    fun setScreenState(playlistId: Int) {
        if (playlistId > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                getPlaylistUseCase.execute(playlistId).collect { playlist ->
                    _screenState.postValue(NewPlaylistState.UpdateOldState(playlist))
                    _playlist.postValue(playlist)
                    _coverSrc.postValue(playlist.coverSrc)
                }
            }
        } else _screenState.value = NewPlaylistState.CreateNewState
    }

    fun updatePlaylist(playlistId: Int, playlistName: Editable?, playlistDescription: Editable?) {
        viewModelScope.launch(Dispatchers.IO) {

            updatePlaylistUseCase.execute(
                playlistId = playlistId,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                uri = _coverSrc.value
            )
        }
    }
}