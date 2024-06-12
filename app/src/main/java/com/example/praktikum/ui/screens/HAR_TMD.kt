package com.example.praktikum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.praktikum.sensors.SensorFusionSensor
import com.example.praktikum.viewModels.SensorViewModel

@Composable
fun HAR_TMD(
    viewModel: SensorViewModel,
    modifier: Modifier = Modifier
) {

    val featureVector = arrayOf(
        viewModel.otherStates?.get(0),
        viewModel.otherStates?.get(1),
        viewModel.otherStates?.get(2),
        viewModel.otherStates?.get(3),
        viewModel.otherStates?.get(4),
        viewModel.otherStates?.get(5),
        viewModel.otherStates?.get(6),
    )

    fun classifyTest(featureVector: Array<MutableState<Float>?>): Double {
        return WekaClassifier.classify(featureVector)
    }

    // Klassifizierungsergebnis
    val classificationResult = classifyTest(featureVector)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        if(viewModel.checked.value) {
            if(classificationResult == 1.0) {
                Text(
                    text = "Stillstand",
                    fontSize = 54.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
            else if(classificationResult == 0.0) {
                Text(
                    text = "Laufen",
                    fontSize = 54.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = "Rennen",
                    fontSize = 54.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Text(
                text = "Bitte Sensoren aktivieren",
                fontSize = 26.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }

    }
}

object WekaClassifier {
    fun classify(i: Array<MutableState<Float>?>): Double {
        var p = Double.NaN

        if (i[6]?.value == null) {
            p = 1.0
        } else if (i[6]?.value!! <= 0.0) {
            if (i[4]?.value == null) {
                p = 1.0
            } else if (i[4]?.value!! <= 0.251285) {
                p = 1.0
            } else if (i[4]?.value!! > 0.251285) {
                p = 0.0
            }
        } else if (i[6]?.value!! > 0.0) {
            if (i[0]?.value == null) {
                p = 0.0
            } else if (i[0]?.value!! <= 1.963008) {
                if (i[6]?.value == null) {
                    p = 0.0
                } else if (i[6]?.value!! <= 4.0) {
                    p = 0.0
                } else if (i[6]?.value!! > 4.0) {
                    p = 2.0
                }
            } else if (i[0]?.value!! > 1.963008) {
                if (i[6]?.value == null) {
                    p = 0.0
                } else if (i[6]?.value!! <= 3.0) {
                    if (i[1]?.value == null) {
                        p = 0.0
                    } else if (i[1]?.value!! <= 1.241613) {
                        p = 0.0
                    } else if (i[1]?.value!! > 1.241613) {
                        p = 2.0
                    }
                } else if (i[6]?.value!! > 3.0) {
                    if (i[1]?.value == null) {
                        p = 2.0
                    } else if (i[1]?.value!! <= 1.578268) {
                        p = 2.0
                    } else if (i[1]?.value!! > 1.578268) {
                        if (i[0]?.value == null) {
                            p = 0.0
                        } else if (i[0]?.value!! <= 2.799087) {
                            p = 0.0
                        } else if (i[0]?.value!! > 2.799087) {
                            p = 2.0
                        }
                    }
                }
            }
        }

        return p
    }
}
