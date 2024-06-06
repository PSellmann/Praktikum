package com.example.praktikum.data

data class GravityMeasuringPoint(
    var timestamp: Long,
    var posX: Float,
    var posY: Float,
    var posZ: Float
) {
    fun toFormattedString(): String {
        return "$timestamp,$posX,$posY,$posZ"
    }
}