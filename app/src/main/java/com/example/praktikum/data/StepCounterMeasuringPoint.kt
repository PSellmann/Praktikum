package com.example.praktikum.data

data class StepCounterMeasuringPoint(
    var timestamp: Long,
    var count: Float
) {
    fun toFormattedString(): String {
        return "$timestamp,$count"
    }
}
