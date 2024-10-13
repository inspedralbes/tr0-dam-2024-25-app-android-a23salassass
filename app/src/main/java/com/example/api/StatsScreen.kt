package com.example.api

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
fun StatsScreen(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val respostesCorrectes = backStackEntry.arguments?.getInt("respostesCorrectes") ?: 0
    val incorrectes = backStackEntry.arguments?.getInt("incorrectes") ?: 0
    val tempsPassat = backStackEntry.arguments?.getInt("tempsPassat") ?: 0

    val minutes = tempsPassat / 60
    val seconds = tempsPassat % 60

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Estadístiques")
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Preguntes encertades: $respostesCorrectes")
        Text(text = "Preguntes fallades: $incorrectes")
        Text(text = "Temps total: $minutes minuts y $seconds segons")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.navigate("home_screen") {
                popUpTo("home_screen") { inclusive = true }
            }
        }) {
            Text(text = "Tornar a jugar")
        }
    }
}
