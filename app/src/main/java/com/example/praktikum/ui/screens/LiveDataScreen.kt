package com.example.praktikum.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktikum.data.SensorDataDisplayInfo
import com.example.praktikum.viewModels.AccelerometerViewModel
import com.example.praktikum.viewModels.GyroscopeViewModel
import com.example.praktikum.viewModels.MagneticFieldViewModel
import com.example.praktikum.viewModels.SensorViewModel

@Composable
fun LiveDataScreen(
    accelerometerViewModel: AccelerometerViewModel,
    gyroscopeViewModel: GyroscopeViewModel,
    magneticFieldViewModel: MagneticFieldViewModel,
    modifier: Modifier = Modifier
) {
    val sensorInfo = mutableListOf(
        SensorDataDisplayInfo("Accelerometer", accelerometerViewModel),
        SensorDataDisplayInfo("Gyroscope", gyroscopeViewModel),
        SensorDataDisplayInfo("Magnetic Field", magneticFieldViewModel)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sensorInfo) { sensorInfo ->
            LiveDataCard(
                sensorName = sensorInfo.name,
                viewModel = sensorInfo.viewModel,
            )
        }
    }
}

@Composable
fun LiveDataCard(
    sensorName: String,
    viewModel: SensorViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(color = Color(0xFF8796A1), shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = sensorName,
                fontSize = 22.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(bottom = 30.dp))

            if (viewModel.checked.value) {
                TextData(viewModel = viewModel)
            } else {
                Text(
                    text = "Keine Live-Daten verfügbar, da der Sensor ausgeschaltet ist!",
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TextData(
    viewModel: SensorViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "X-Koordinate:" + viewModel.positionStates[0].value.toString(),
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Y-Koordinate:" + viewModel.positionStates[1].value.toString(),
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Z-Koordinate:" + viewModel.positionStates[2].value.toString(),
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}