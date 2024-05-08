package com.example.praktikum.viewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

abstract class SensorViewModel: ViewModel() {
    abstract val positionStates: MutableList<MutableState<Float>>
    abstract val checked: MutableState<Boolean>
    abstract val samplingRate: MutableState<Float>
}