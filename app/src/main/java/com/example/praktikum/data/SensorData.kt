package com.example.praktikum.data

import android.content.Context
import android.os.Environment
import com.example.praktikum.sensors.MagneticFieldSensor
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

object SensorData {
    var accelerometerDataList: MutableList<AccelerometerMeasuringPoint> = mutableListOf()
    var gyroscopeDataList: MutableList<GyroscopeMeasuringPoint> = mutableListOf()
    var magneticFieldDataList: MutableList<MagneticFieldMeasuringPoint> = mutableListOf()
}

fun saveToFileInDownloadsDirectory(context: Context, fileName: String, sensorData: List<Any> ): Boolean {
    val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    if (!downloadsDirectory.exists()) {
        if (!downloadsDirectory.mkdirs()) {
            return false
        }
    }

    val file = File(downloadsDirectory, fileName)

    return try {
        BufferedWriter(OutputStreamWriter(FileOutputStream(file))).use { writer ->
            for (data in sensorData) {
                val line = when (data) {
                    is AccelerometerMeasuringPoint -> data.toFormattedString()
                    is GyroscopeMeasuringPoint -> data.toFormattedString()
                    is MagneticFieldMeasuringPoint -> data.toFormattedString()
                    else -> {
                        "Unbekannteer Datentyp: $data"
                    }
                }
                writer.write(line)
                writer.newLine()
            }
        }
        true // Erfolgreich gespeichert
    } catch (e: IOException) {
        e.printStackTrace()
        false // Fehler beim Speichern
    }
}