package com.example.praktikum.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.praktikum.data.SensorData
import com.example.praktikum.data.SensorFusionMeasuringPoint
import com.example.praktikum.data.TransportationModeRawData
import com.example.praktikum.data.TransportationRawDataPoint
import com.example.praktikum.viewModels.SensorViewModel
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object SensorFusionSensor: AbstractSensor() {
    var sensorManager: SensorManager? = null
    var sensorEventListenerAccelerometer: SensorEventListener? = null
    var sensorEventListenerGyroscope: SensorEventListener? = null
    var sensorEventListenerGravity: SensorEventListener? = null
    var sensorEventListenerStepCounter: SensorEventListener? = null
    var viewModel: SensorViewModel? = null

    var counter = 0

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
                                viewModel!!.positionStates.getOrNull(8)!!.value,
                                viewModel!!.positionStates.getOrNull(9)!!.value,
                            )
                        )

                        TransportationModeRawData.TransportationDataList.add(
                            TransportationRawDataPoint(
                                viewModel!!.positionStates.getOrNull(3)!!.value,
                                viewModel!!.positionStates.getOrNull(4)!!.value,
                                viewModel!!.positionStates.getOrNull(5)!!.value,
                                viewModel!!.positionStates.getOrNull(9)!!.value,
                            )
                        )

                        counter++
                        if(counter == 1000) {
                            calculateFeatures(TransportationModeRawData.TransportationDataList, viewModel!!
                            )
                            counter = 0
                            TransportationModeRawData.TransportationDataList.clear()
                        }
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

        if(this.sensorEventListenerStepCounter == null) {
            this.sensorEventListenerStepCounter = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                }
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                        viewModel?.positionStates?.getOrNull(9)?.value = event.values[0]
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

        this.sensorManager!!.registerListener(this.sensorEventListenerStepCounter,
            this.sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),samplingRate)
    }

    override fun stopListening() {
        this.sensorManager?.unregisterListener(this.sensorEventListenerAccelerometer)
        this.sensorManager?.unregisterListener(this.sensorEventListenerGyroscope)
        this.sensorManager?.unregisterListener(this.sensorEventListenerGravity)
        this.sensorManager?.unregisterListener(this.sensorEventListenerStepCounter)
        this.sensorEventListenerAccelerometer = null
        this.sensorEventListenerGyroscope = null
        this.sensorEventListenerGravity = null
        this.sensorEventListenerStepCounter = null
        this.sensorManager = null
        SensorData.sensorFusionDataList.clear()
        TransportationModeRawData.TransportationDataList.clear()
    }
}

fun calculateFeatures(window: MutableList<TransportationRawDataPoint>, viewModel: SensorViewModel) {
    // Gyroscope
    val gyroMagnitude = window.map { sqrt(it.posX * it.posX + it.posY * it.posY + it.posZ * it.posZ) }
    val meanGyro = gyroMagnitude.average().toFloat()
    val stdGyro = gyroMagnitude.map { (it - meanGyro).pow(2) }.average().let { sqrt(it).toFloat() }
    val varGyro = stdGyro.pow(2)
    val maxGyro = gyroMagnitude.maxOrNull() ?: 0.0f
    val minGyro = gyroMagnitude.minOrNull() ?: 0.0f
    val smaGyro = gyroMagnitude.sumByDouble { abs(it).toDouble() } / window.size.toFloat()

    // Steps
    val stepCounts = window.map { it.steps }
    val stepsMin = stepCounts.minOrNull() ?: 0.0f
    val stepsMax = stepCounts.maxOrNull() ?: 0.0f
    val stepsDifference = abs(stepsMin - stepsMax)

    viewModel.otherStates?.getOrNull(0)?.value = meanGyro
    viewModel.otherStates?.getOrNull(1)?.value = stdGyro
    viewModel.otherStates?.getOrNull(3)?.value = maxGyro
    viewModel.otherStates?.getOrNull(4)?.value = minGyro
    viewModel.otherStates?.getOrNull(5)?.value = smaGyro.toFloat()
    viewModel.otherStates?.getOrNull(6)?.value = stepsDifference

    // Logge die berechneten Merkmale
    Log.d("StatisticalFeatures", "Mean Gyro: ${viewModel.otherStates?.get(0)?.value}, Std Gyro: ${viewModel.otherStates?.get(1)?.value}, Var Gyro: ${viewModel.otherStates?.get(2)?.value}, Max Gyro: ${viewModel.otherStates?.get(3)?.value}, Min Gyro: ${viewModel.otherStates?.get(4)?.value}, SMA Gyro: ${viewModel.otherStates?.get(5)?.value}")
    Log.d("StatisticalFeatures", "Steps Difference: ${viewModel.otherStates?.get(6)?.value}")
}

