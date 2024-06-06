package com.example.praktikum.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.praktikum.data.SensorData
import com.example.praktikum.data.SensorFusionMeasuringPoint
import com.example.praktikum.viewModels.SensorViewModel

object SensorFusionSensor: AbstractSensor() {
    var sensorManager: SensorManager? = null
    var sensorEventListenerAccelerometer: SensorEventListener? = null
    var sensorEventListenerGyroscope: SensorEventListener? = null
    var sensorEventListenerGravity: SensorEventListener? = null
    var viewModel: SensorViewModel? = null


    override fun startListening(ctx: Context, samplingRate: Int) {
        if(this.sensorManager == null) {
            this.sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        if(this.sensorEventListenerAccelerometer == null) {
            this.sensorEventListenerAccelerometer = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                }
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        viewModel?.positionStates?.getOrNull(0)?.value = event.values[0]
                        viewModel?.positionStates?.getOrNull(1)?.value = event.values[1]
                        viewModel?.positionStates?.getOrNull(2)?.value = event.values[2]

                        SensorData.sensorFusionDataList.add(
                            SensorFusionMeasuringPoint(
                                System.currentTimeMillis(),
                                event.values[0],
                                event.values[1],
                                event.values[2],
                                viewModel!!.positionStates.getOrNull(3)!!.value,
                                viewModel!!.positionStates.getOrNull(4)!!.value,
                                viewModel!!.positionStates.getOrNull(5)!!.value,
                                viewModel!!.positionStates.getOrNull(6)!!.value,
                                viewModel!!.positionStates.getOrNull(7)!!.value,
                                viewModel!!.positionStates.getOrNull(8)!!.value
                            )
                        )
                    }
                }
            }
        }

        if(this.sensorEventListenerGyroscope == null) {
            this.sensorEventListenerGyroscope = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                }
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                        viewModel?.positionStates?.getOrNull(3)?.value = event.values[0]
                        viewModel?.positionStates?.getOrNull(4)?.value = event.values[1]
                        viewModel?.positionStates?.getOrNull(5)?.value = event.values[2]
                    }
                }
            }
        }

        if(this.sensorEventListenerGravity == null) {
            this.sensorEventListenerGravity = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                }
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_GRAVITY) {
                        viewModel?.positionStates?.getOrNull(6)?.value = event.values[0]
                        viewModel?.positionStates?.getOrNull(7)?.value = event.values[1]
                        viewModel?.positionStates?.getOrNull(8)?.value = event.values[2]
                    }
                }
            }
        }

        this.sensorManager!!.registerListener(this.sensorEventListenerAccelerometer,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),samplingRate)

        this.sensorManager!!.registerListener(this.sensorEventListenerGyroscope,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE),samplingRate)

        this.sensorManager!!.registerListener(this.sensorEventListenerGravity,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY),samplingRate)
    }

    override fun stopListening() {
        this.sensorManager?.unregisterListener(this.sensorEventListenerAccelerometer)
        this.sensorManager?.unregisterListener(this.sensorEventListenerGyroscope)
        this.sensorManager?.unregisterListener(this.sensorEventListenerGravity)
        this.sensorEventListenerAccelerometer = null
        this.sensorEventListenerGyroscope = null
        this.sensorEventListenerGravity = null
        this.sensorManager = null
        SensorData.sensorFusionDataList.clear()
    }
}