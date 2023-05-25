package com.example.layoutmake.domain.usecases

import android.hardware.camera2.params.DeviceStateSensorOrientationMap
import com.example.layoutmake.domain.api.PlayerRepository

class ReleasePlayerUseCase(
    private val playerRepository: PlayerRepository
) {
    fun execute(){
        playerRepository.releasePlayer()
    }
}