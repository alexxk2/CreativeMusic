package com.example.layoutmake.domain.usecases

import androidx.lifecycle.LiveData
import com.example.layoutmake.domain.api.PlayerRepository

class GetPlayerStateUseCase(
    private val playerRepository: PlayerRepository
) {
    fun execute():LiveData<Int> = playerRepository.getPlayerState()
}