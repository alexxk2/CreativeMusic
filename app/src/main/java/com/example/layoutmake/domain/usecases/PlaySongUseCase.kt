package com.example.layoutmake.domain.usecases

import com.example.layoutmake.domain.api.PlayerRepository

class PlaySongUseCase(
    private val playerRepository: PlayerRepository
) {

    fun execute() {
        playerRepository.playSong()
    }
}