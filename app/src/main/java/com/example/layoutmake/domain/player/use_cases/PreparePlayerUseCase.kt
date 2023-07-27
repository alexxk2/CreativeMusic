package com.example.layoutmake.domain.player.use_cases

import com.example.layoutmake.domain.repositories.PlayerRepository

class PreparePlayerUseCase(
    private val playerRepository: PlayerRepository
) {
    fun execute(path: String) {
        playerRepository.preparePlayer(path = path)
    }
}