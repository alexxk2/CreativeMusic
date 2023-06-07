package com.example.layoutmake.domain.usecases

import com.example.layoutmake.data.OnCompletePlaying
import com.example.layoutmake.data.OnPreparePlayer
import com.example.layoutmake.domain.api.PlayerRepository

class PreparePlayerUseCase(
    private val playerRepository: PlayerRepository
) {

    fun execute(
        path: String,
        onComplete: OnCompletePlaying,
        onPreparePlayer: OnPreparePlayer
    ) {
        playerRepository.preparePlayer(path = path, onComplete = onComplete, onPreparePlayer = onPreparePlayer)
    }
}