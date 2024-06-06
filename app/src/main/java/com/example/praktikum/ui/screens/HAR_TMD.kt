package com.example.praktikum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.praktikum.viewModels.SensorFusionViewModel
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun HAR_TMD(
    sensorFusionViewModel: SensorFusionViewModel,
    modifier: Modifier = Modifier
) {
    val sensorValues = sensorFusionViewModel.positionStates.map { it.value.toDouble() }

    // Statistische Kenngrößen berechnen für Beschleunigung
    val meanAcc = calculateMean(sensorValues.subList(0,3))
    val stdAcc = calculateStandardDeviation(sensorValues.subList(0,3))
    val varAcc = calculateVariance(sensorValues.subList(0,3))
    val maxAcc = calculateMax(sensorValues.subList(0,3))
    val minAcc = calculateMin(sensorValues.subList(0,3))
    val smaAcc = calculateSMA(sensorValues.subList(0,3))

    // Statistische Kenngrößen berechnen für Gyroskop
    val meanGyro = calculateMean(sensorValues.subList(3,6))
    val stdGyro = calculateStandardDeviation(sensorValues.subList(3,6))
    val varGyro = calculateVariance(sensorValues.subList(3,6))
    val maxGyro = calculateMax(sensorValues.subList(3,6))
    val minGyro = calculateMin(sensorValues.subList(3,6))
    val smaGyro = calculateSMA(sensorValues.subList(3,6))

    // Statistische Kenngrößen berechnen für Gravitation
    val meanGrav = calculateMean(sensorValues.subList(6,9))
    val stdGrav = calculateStandardDeviation(sensorValues.subList(6,9))
    val varGrav = calculateVariance(sensorValues.subList(6,9))
    val maxGrav = calculateMax(sensorValues.subList(6,9))
    val minGrav = calculateMin(sensorValues.subList(6,9))
    val smaGrav = calculateSMA(sensorValues.subList(6,9))

    val featureVector = arrayOf(
        meanAcc, stdAcc, varAcc, maxAcc, minAcc, smaAcc,
        meanGyro, stdGyro, varGyro, maxGyro, minGyro, smaGyro,
        meanGrav, stdGrav, varGrav, maxGrav, minGrav, smaGrav
    )

    fun classify(featureVector: Array<Double>): Double {
        return WekaClassifier.classify(featureVector)
    }

    // Klassifizierungsergebnis
    val classificationResult = classify(featureVector)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        if(sensorFusionViewModel.checked.value) {
            if(classificationResult == 0.0)
                Text(
                    text = "Stillstand",
                    fontSize = 54.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            else if(classificationResult == 1.0) {
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

// Funktionen zur Berechnung statistischer Kenngrößen

fun calculateMean(values: List<Double>): Double {
    return if (values.isNotEmpty()) {
        values.filterNotNull().average()
    } else {
        0.0
    }
}

fun calculateStandardDeviation(values: List<Double>): Double {
    val mean = calculateMean(values)
    val filteredValues = values.filterNotNull()
    val variance = filteredValues.map { (it - mean).pow(2) }.average()
    return sqrt(variance)
}

fun calculateVariance(values: List<Double>): Double {
    val mean = calculateMean(values)
    val filteredValues = values.filterNotNull()
    val variance = filteredValues.map { (it - mean).pow(2) }.average()
    return variance
}

fun calculateMax(values: List<Double>): Double {
    return values.filterNotNull().maxOrNull() ?: 0.0
}

fun calculateMin(values: List<Double>): Double {
    return values.filterNotNull().minOrNull() ?: 0.0
}

fun calculateSMA(values: List<Double>): Double {
    return if (values.isNotEmpty()) {
        values.filterNotNull().sum() / values.size
    } else {
        0.0
    }
}


object WekaClassifier {
    fun classify(i: Array<Double>): Double {
        // Decision Tree-Klassifizierung basierend auf den übergebenen Feature-Werten
        var p = Double.NaN
        if (i[2] <= 2.3025925150874933) {
            if (i[2] <= 0.8801072982076212) {
                p = 1.0
            } else {
                if (i[0] <= 10.099052960801234) {
                    if (i[10] <= 0.2631639231368004) {
                        p = 1.0
                    } else {
                        if (i[4] <= 7.880004193595666) {
                            if (i[6] <= 1.0055309154069174) {
                                p = 0.0
                            } else {
                                p = 1.0
                            }
                        } else {
                            p = 0.0
                        }
                    }
                } else {
                    p = 0.0
                }
            }
        } else {
            if (i[5] <= 24.01519051833) {
                if (i[11] <= 4.985025865879999) {
                    if (i[1] <= 2.3387430054140963) {
                        if (i[9] <= 2.339093305166269) {
                            p = 0.0
                        } else {
                            if (i[5] <= 15.06857880325) {
                                if (i[1] <= 1.8934348516441835) {
                                    if (i[3] <= 14.498216568035335) {
                                        if (i[3] <= 13.222346981789455) {
                                            p = 1.0
                                        } else {
                                            if (i[4] <= 5.939144129729691) {
                                                p = 0.0
                                            } else {
                                                p = 1.0
                                            }
                                        }
                                    } else {
                                        p = 1.0
                                    }
                                } else {
                                    if (i[10] <= 0.30346617471136594) {
                                        if (i[3] <= 15.637416294396289) {
                                            if (i[9] <= 2.7269280911774434) {
                                                p = 0.0
                                            } else {
                                                p = 1.0
                                            }
                                        } else {
                                            p = 1.0
                                        }
                                    } else {
                                        p = 0.0
                                    }
                                }
                            } else {
                                if (i[4] <= 5.949487372314162) {
                                    if (i[0] <= 10.230108032001525) {
                                        p = 1.0
                                    } else {
                                        p = 0.0
                                    }
                                } else {
                                    if (i[4] <= 8.441640531182786) {
                                        p = 0.0
                                    } else {
                                        if (i[1] <= 1.9164844468128295) {
                                            p = 0.0
                                        } else {
                                            p = 1.0
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        p = 0.0
                    }
                } else {
                    if (i[1] <= 10.239042262629034) {
                        if (i[7] <= 1.2334146471775107) {
                            p = 2.0
                        } else {
                            if (i[5] <= 22.370677010795) {
                                p = 0.0
                            } else {
                                if (i[7] <= 1.813028494056571) {
                                    p = 2.0
                                } else {
                                    p = 0.0
                                }
                            }
                        }
                    } else {
                        p = 2.0
                    }
                }
            } else {
                p = 2.0
            }
        }
        return p
    }
}