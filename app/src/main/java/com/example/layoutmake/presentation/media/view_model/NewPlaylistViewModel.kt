package com.example.layoutmake.presentation.media.view_model

import android.net.Uri
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.AddNewPlaylistUseCase
import com.example.layoutmake.domain.media.SaveImageAndReturnPathUseCase
import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val addNewPlaylistUseCase: AddNewPlaylistUseCase,
    private val saveImageAndReturnPathUseCase: SaveImageAndReturnPathUseCase
) : ViewModel() {

    private val _track = MutableLiveData<Track?>(null)
    val track: LiveData<Track?> = _track

    private val _coverSrc = MutableLiveData<String?>(null)
    val coverSrc: LiveData<String?> = _coverSrc

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _coverSrc.postValue(saveImageAndReturnPathUseCase.execute(uri))
        }
    }

    fun getTrack(track: Track?){
        _track.value = track
    }

    fun addNewPlaylist(playlistName: Editable?, playlistDescription: Editable?) {
        viewModelScope.launch {
            addNewPlaylistUseCase.execute(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                coverSrc = _coverSrc.value
            )
        }
    }
}