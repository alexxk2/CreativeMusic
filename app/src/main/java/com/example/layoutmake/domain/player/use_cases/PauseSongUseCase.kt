package com.example.layoutmake.domain.player.use_cases

import com.example.layoutmake.domain.repositories.PlayerRepository

class PauseSongUseCase(
    private val playerRepository: PlayerRepository
) {
    fun execute(){
        playerRepository.pauseSong()
    }
}