package com.example.praktikum.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.praktikum.data.AccelerometerMeasuringPoint
import com.example.praktikum.data.SensorData
import com.example.praktikum.data.StepCounterMeasuringPoint
import com.example.praktikum.viewModels.AccelerometerViewModel
import com.example.praktikum.viewModels.SensorViewModel

object StepCounterSensor: AbstractSensor() {
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
                    if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                        viewModel?.positionStates?.getOrNull(0)?.value = event.values[0]

                        SensorData.stepCounterDataList.add(
                            StepCounterMeasuringPoint(
                                System.currentTimeMillis(),
                                event.values[0]
                            )
                        )
                    }
                }
            }
        }

        this.sensorManager!!.registerListener(this.sensorEventListener,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),samplingRate)
    }

    override fun stopListening() {
        this.sensorManager?.unregisterListener(this.sensorEventListener)
        this.sensorEventListener = null
        this.sensorManager = null
        SensorData.stepCounterDataList.clear()
    }
}