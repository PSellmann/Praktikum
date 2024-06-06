package com.example.praktikum.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.praktikum.ui.screens.Screens

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val listOfNavItems: List<NavItem> = listOf(
    NavItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = Screens.Home.name
    ),
    NavItem(
        label = "Settings",
        icon = Icons.Default.Settings,
        route = Screens.Settings.name
    ),
    NavItem(
        label = "LiveData",
        icon = Icons.Default.Check,
        route = Screens.LiveDataScreen.name
    ),
    NavItem(
        label = "HAR_TMD",
        icon = Icons.Default.Info,
        route = Screens.HAR_TMD.name
    )

)
