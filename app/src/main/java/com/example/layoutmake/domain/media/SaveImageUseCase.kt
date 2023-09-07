package com.example.layoutmake.domain.media

import android.net.Uri
import com.example.layoutmake.domain.repositories.MediaRepository

class SaveImageUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(uri:Uri): Uri = mediaRepository.saveImageAndReturnPath(uri)
}