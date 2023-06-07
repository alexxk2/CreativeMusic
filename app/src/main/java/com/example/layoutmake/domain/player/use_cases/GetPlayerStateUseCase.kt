package com.example.layoutmake.domain.player.use_cases

import com.example.layoutmake.data.repositories.player.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GetPlayerStateUseCase(private val playerRepository: PlayerRepository) {

    fun execute(): Flow<Int> = playerRepository.getPlayerState()

}