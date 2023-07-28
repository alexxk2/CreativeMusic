package com.example.layoutmake.domain.player.use_cases

import com.example.layoutmake.domain.repositories.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerStateUseCase(private val playerRepository: PlayerRepository) {

    fun execute(): Flow<Int> = playerRepository.getPlayerState()

}