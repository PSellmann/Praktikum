package com.example.praktikum.data

import com.example.praktikum.sensors.AbstractSensor
import com.example.praktikum.viewModels.SensorViewModel

data class SensorInfo(
    val name: String,
    val viewModel: SensorViewModel,
    val sensor: AbstractSensor,
    val dataList: List<Any>
)