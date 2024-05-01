package com.example.praktikum.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.praktikum.data.AccelerometerMeasuringPoint
import com.example.praktikum.data.SensorData
import com.example.praktikum.viewModels.AccelerometerViewModel

object AccelerometerSensor {
    var sensorManager: SensorManager? = null
    var sensorEventListener: SensorEventListener? = null
    var viewModel: AccelerometerViewModel? = null


    fun startListening(ctx: Context) {
        if(this.sensorManager == null) {
            this.sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        if(this.sensorEventListener == null) {
            this.sensorEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                }
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        viewModel?.posX = event.values[0]
                        viewModel?.posY = event.values[1]
                        viewModel?.posZ = event.values[2]

                        SensorData.accelerometerDataList.add(AccelerometerMeasuringPoint(
                            event.values[0],
                            event.values[1],
                            event.values[2]
                        ))

                        //Log.d("Acc", "${event.values[0]} ${event.values[1]} ${event.values[2]}")
                    }
                }
            }
        }

        this.sensorManager!!.registerListener(this.sensorEventListener,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopListening() {
        this.sensorManager?.unregisterListener(this.sensorEventListener)
        this.sensorEventListener = null
        this.sensorManager = null
        SensorData.accelerometerDataList.clear()
    }
}