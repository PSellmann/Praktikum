package com.example.praktikum.viewModels

import androidx.compose.runtime.mutableStateOf

class AccelerometerViewModel: SensorViewModel() {
    override var positionStates = mutableListOf(
        mutableStateOf(0.0F),
        mutableStateOf(0.0F),
        mutableStateOf(0.0F),
    )

    override val checked = mutableStateOf(false)
}