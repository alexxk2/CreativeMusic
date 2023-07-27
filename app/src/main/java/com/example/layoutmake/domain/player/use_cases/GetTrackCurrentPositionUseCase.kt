package com.example.layoutmake.domain.player.use_cases

import com.example.layoutmake.domain.repositories.PlayerRepository

class GetTrackCurrentPositionUseCase(
    private val playerRepository: PlayerRepository
) {

    fun execute(): Int =  playerRepository.getTrackCurrentPosition()

}