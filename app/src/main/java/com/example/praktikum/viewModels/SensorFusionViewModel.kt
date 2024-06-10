package com.example.praktikum.viewModels

import androidx.compose.runtime.mutableStateOf

class SensorFusionViewModel: SensorViewModel() {
    override var positionStates = mutableListOf(
        mutableStateOf(0.0F), //Acceleromter x
        mutableStateOf(0.0F), //Acceleromter y
        mutableStateOf(0.0F), //Acceleromter z
        mutableStateOf(0.0F), //Gyroscope x
        mutableStateOf(0.0F), //Gyroscope y
        mutableStateOf(0.0F), //Gyroscope z
        mutableStateOf(0.0F), //Gravity x
        mutableStateOf(0.0F), //Gravity y
        mutableStateOf(0.0F), //Gravity z
        mutableStateOf(0.0F), //Steps
    )

    override val checked = mutableStateOf(false)

    override val samplingRate = mutableStateOf(3.0F)

    override val otherStates = mutableListOf(
        mutableStateOf(0.0f),
        mutableStateOf(0.0f),
        mutableStateOf(0.0f),
        mutableStateOf(0.0f),
        mutableStateOf(0.0f),
        mutableStateOf(0.0f),
        mutableStateOf(0.0f),
    )
}