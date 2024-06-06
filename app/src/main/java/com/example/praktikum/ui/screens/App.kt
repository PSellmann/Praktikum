package com.example.navtest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.praktikum.sensors.AccelerometerSensor
import com.example.praktikum.sensors.GravitySensor
import com.example.praktikum.sensors.GyroscopeSensor
import com.example.praktikum.sensors.MagneticFieldSensor
import com.example.praktikum.sensors.SensorFusionSensor
import com.example.praktikum.ui.navigation.listOfNavItems
import com.example.praktikum.ui.screens.HAR_TMD
import com.example.praktikum.ui.screens.Screens
import com.example.praktikum.ui.screens.Settings
import com.example.praktikum.ui.screens.Home
import com.example.praktikum.ui.screens.LiveDataScreen
import com.example.praktikum.viewModels.AccelerometerViewModel
import com.example.praktikum.viewModels.GravityViewModel
import com.example.praktikum.viewModels.GyroscopeViewModel
import com.example.praktikum.viewModels.MagneticFieldViewModel
import com.example.praktikum.viewModels.SensorFusionViewModel

@Composable
fun App() {
    var navController: NavHostController = rememberNavController()

    val accelerometerViewModel = viewModel<AccelerometerViewModel>()
    AccelerometerSensor.viewModel = accelerometerViewModel

    val gyroscopeViewModel = viewModel<GyroscopeViewModel>()
    GyroscopeSensor.viewModel = gyroscopeViewModel

    val magneticFieldViewModel = viewModel<MagneticFieldViewModel>()
    MagneticFieldSensor.viewModel = magneticFieldViewModel

    val sensorFusionViewModel = viewModel<SensorFusionViewModel>()
    SensorFusionSensor.viewModel = sensorFusionViewModel

    val gravityViewModel = viewModel<GravityViewModel>()
    GravitySensor.viewModel = gravityViewModel

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry  by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOfNavItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {it.route == navItem.route} == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.Home.name) {
                Home()
            }

            composable(route = Screens.Settings.name) {
                Settings(
                    accelerometerViewModel = accelerometerViewModel,
                    gyroscopeViewModel = gyroscopeViewModel,
                    magneticFieldViewModel = magneticFieldViewModel,
                    sensorFusionViewModel = sensorFusionViewModel,
                    gravityViewModel = gravityViewModel
                )
            }

            composable(route = Screens.LiveDataScreen.name) {
                LiveDataScreen(
                    accelerometerViewModel = accelerometerViewModel,
                    gyroscopeViewModel = gyroscopeViewModel,
                    magneticFieldViewModel = magneticFieldViewModel,
                    sensorFusionViewModel = sensorFusionViewModel,
                    gravityViewModel
                )
            }

            composable(route = Screens.HAR_TMD.name) {
                HAR_TMD(
                    sensorFusionViewModel = sensorFusionViewModel
                )
            }
        }
    }
}