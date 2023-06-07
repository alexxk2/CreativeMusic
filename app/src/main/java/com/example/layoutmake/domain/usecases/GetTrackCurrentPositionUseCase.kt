package com.example.layoutmake.domain.usecases

import com.example.layoutmake.domain.api.PlayerRepository

class GetTrackCurrentPositionUseCase(
    private val playerRepository: PlayerRepository
) {

    fun execute(): Int =  playerRepository.getTrackCurrentPosition()

}