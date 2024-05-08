package com.example.praktikum.data

data class AccelerometerMeasuringPoint(
    var timestamp: Long,
    var posX: Float,
    var posY: Float,
    var posZ: Float
) {
    fun toFormattedString(): String {
        return "$timestamp,$posX,$posY,$posZ"
    }
}