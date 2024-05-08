package com.example.praktikum.sensors

import android.content.Context

abstract class AbstractSensor {
    abstract fun startListening(context: Context, samplingRate: Int)
    abstract fun stopListening()
}