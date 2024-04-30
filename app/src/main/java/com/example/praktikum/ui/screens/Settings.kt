package com.example.praktikum.ui.screens

import android.hardware.Sensor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.praktikum.sensors.AccelerometerSensor

@Composable
fun Settings(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        AccelerometerCard(sensorName = "Acclereometer")
    }
}
@Composable
fun AccelerometerCard(sensorName: String, modifier: Modifier = Modifier) {
    var ctx = LocalContext.current
    var checked by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
    ) {
        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = sensorName)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = !checked

                        if(checked) {
                            AccelerometerSensor.startListening(ctx)
                        } else {
                            AccelerometerSensor.stopListening()
                        }
                    }
                )
            }

            if(checked) {
                Text(
                    text = "X-Koordinate:",
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Y-Koordinate:",
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Z-Koordinate:",
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}