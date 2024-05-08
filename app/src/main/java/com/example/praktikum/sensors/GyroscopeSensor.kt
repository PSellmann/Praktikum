package com.example.praktikum.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.praktikum.data.GyroscopeMeasuringPoint
import com.example.praktikum.data.SensorData
import com.example.praktikum.viewModels.SensorViewModel

object GyroscopeSensor: AbstractSensor() {
    var sensorManager: SensorManager? = null
    var sensorEventListener: SensorEventListener? = null
    var viewModel: SensorViewModel? = null


    override fun startListening(ctx: Context, samplingRate: Int) {
        if(this.sensorManager == null) {
            this.sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        if(this.sensorEventListener == null) {
            this.sensorEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                }
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                        viewModel?.positionStates?.getOrNull(0)?.value = event.values[0]
                        viewModel?.positionStates?.getOrNull(1)?.value = event.values[1]
                        viewModel?.positionStates?.getOrNull(2)?.value = event.values[2]

                        SensorData.gyroscopeDataList.add(
                            GyroscopeMeasuringPoint(
                                System.currentTimeMillis(),
                                event.values[0],
                                event.values[1],
                                event.values[2]
                            )
                        )
                        //Log.d("Gyr", "${event.values[0]} ${event.values[1]} ${event.values[2]}")
                    }
                }
            }
        }

        this.sensorManager!!.registerListener(this.sensorEventListener,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),samplingRate)
    }

    override fun stopListening() {
        this.sensorManager?.unregisterListener(this.sensorEventListener)
        SensorData.gyroscopeDataList.clear()
        this.sensorEventListener = null
        this.sensorManager = null
    }
}