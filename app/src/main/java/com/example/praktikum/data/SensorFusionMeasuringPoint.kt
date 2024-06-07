package com.example.praktikum.data

data class SensorFusionMeasuringPoint(
    var timestamp: Long,
    var posXAcc: Float,
    var posYAcc: Float,
    var posZAcc: Float,
    var posXGyr: Float,
    var posYGyr: Float,
    var posZGyr: Float,
    var posXGrav: Float,
    var posYGrav: Float,
    var posZGrav: Float,
    var countSteps: Float,
) {
    fun toFormattedString(): String {
        return "$timestamp,$posXAcc,$posYAcc,$posZAcc,$posXGyr,$posYGyr,$posZGyr, $posXGrav,$posYGrav,$posZGrav,$countSteps"
    }
}
