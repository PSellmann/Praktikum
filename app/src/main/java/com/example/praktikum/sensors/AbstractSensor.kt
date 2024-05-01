package com.example.praktikum.sensors

import android.content.Context

abstract class AbstractSensor {
    abstract fun startListening(context: Context)
    abstract fun stopListening()
}