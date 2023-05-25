package com.example.layoutmake.domain.usecases

import com.example.layoutmake.domain.api.PlayerRepository

class PreparePlayerUseCase(
    private val playerRepository: PlayerRepository
) {

    fun execute(
        path: String
    ) {
        playerRepository.preparePlayer(path = path)
    }
}