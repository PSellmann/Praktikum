package com.example.praktikum.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AccelerometerViewModel: ViewModel() {
    var posX by mutableStateOf(0.0F)
    var posY by mutableStateOf(0.0F)
    var posZ by mutableStateOf(0.0F)

    var checked by mutableStateOf(false)
}