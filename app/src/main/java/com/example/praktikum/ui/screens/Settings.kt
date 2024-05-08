package com.example.praktikum.ui.screens

import android.hardware.Sensor
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.praktikum.data.SensorData
import com.example.praktikum.data.SensorInfo
import com.example.praktikum.data.saveToFileInDownloadsDirectory
import com.example.praktikum.sensors.AbstractSensor
import com.example.praktikum.sensors.AccelerometerSensor
import com.example.praktikum.sensors.GyroscopeSensor
import com.example.praktikum.viewModels.AccelerometerViewModel
import com.example.praktikum.viewModels.GyroscopeViewModel
import com.example.praktikum.viewModels.SensorViewModel
import kotlin.math.roundToInt

@Composable
fun Settings(modifier: Modifier = Modifier) {
    val accelerometerViewModel = viewModel<AccelerometerViewModel>()
    AccelerometerSensor.viewModel = accelerometerViewModel

    val gyroscopeViewModel = viewModel<GyroscopeViewModel>()
    GyroscopeSensor.viewModel = gyroscopeViewModel

    val sensors = listOf(
        SensorInfo("Accelerometer", accelerometerViewModel, AccelerometerSensor, SensorData.accelerometerDataList),
        SensorInfo("Gyroscope", gyroscopeViewModel, GyroscopeSensor, SensorData.gyroscopeDataList)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sensors) { sensorInfo ->
            SensorCard(
                sensorName = sensorInfo.name,
                viewModel = sensorInfo.viewModel,
                sensor = sensorInfo.sensor,
                dataList = sensorInfo.dataList
            )
        }
    }
}


@Composable
fun SensorCard(
    sensorName: String,
    viewModel: SensorViewModel,
    sensor: AbstractSensor,
    dataList: List<Any>,
    modifier: Modifier = Modifier
) {
    var ctx = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(color = Color(0xFF8796A1), shape = RoundedCornerShape(8.dp))
    ) {
        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = sensorName,
                    color = Color.White,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.checked.value,
                    onCheckedChange = {
                        viewModel.checked.value = !viewModel.checked.value

                        if(viewModel.checked.value) {
                            sensor.startListening(ctx, viewModel.samplingRate.value.toInt())
                        } else {
                            sensor.stopListening()
                        }
                    }
                )
            }

            Slider(
                value = viewModel.samplingRate.value,
                onValueChange = {
                    viewModel.samplingRate.value = it
                },
                valueRange = 0f..3f,
                steps = 2,
                enabled = !viewModel.checked.value,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            val samplingRateText = when (viewModel.samplingRate.value) {
                0.0F -> "SENSOR_DELAY_FASTEST"
                1.0F -> "SENSOR_DELAY_GAME"
                2.0F -> "SENSOR_DELAY_UI"
                3.0F -> "SENSOR_DELAY_NORMAL"
                else -> "Unbekannte Sampling-Rate"
            }

            Text(
                text = samplingRateText,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )



            if(viewModel.checked.value) {
                viewModel.positionStates.forEachIndexed { index, state ->
                    Text(
                        text = when (index) {
                            0 -> "X-Koordinate: ${state.value}"
                            1 -> "Y-Koordinate: ${state.value}"
                            2 -> "Z-Koordinate: ${state.value}"
                            else -> ""
                        },
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                }

                SaveDataButton(
                    viewModel,
                    dataList,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun SaveDataButton(viewModel: SensorViewModel, sensorData: List<Any>, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }

    if (showDialog.value) {
        PopupWithTextField(
            onDismiss = {
                showDialog.value = false
            },
            onTextSubmitted = {
                Log.d("Dateiname", it)
                showDialog.value = false
                viewModel.checked.value = false
                saveToFileInDownloadsDirectory(ctx, it + ".txt", sensorData)
            }
        )
    }

    Button(
        onClick = {
            showDialog.value = true
        },
        modifier = modifier.padding(20.dp)
    ) {
        Text(
            text = "Speichern",
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

@Composable
fun PopupWithTextField(
    onDismiss: () -> Unit,
    onTextSubmitted: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Dateiname eingeben") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("Dateiname") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onTextSubmitted(textFieldValue.text)
                    onDismiss()
                }
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Abbrechen")
            }
        }
    )
}