package com.example.sensebridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val homeScreenState = HomeScreenState(
                showAddSessionDialog = remember { mutableStateOf(false) },
                voiceButtonPressed = remember { mutableStateOf(false) },
                sessionCount = remember { mutableIntStateOf(0) },
                selectedFeature = remember { mutableStateOf(FeatureInfo("Invalid", 0)) },
                isFeatureSelected = remember { mutableStateOf(false) }
            )

            val homeScreen = HomeScreen()
            val sceneDescription = SceneDescription()
            val screen3 = Screen3()

            val navController = rememberNavController()
            NavHost(navController = navController,
                startDestination = "home",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable("home") { homeScreen.HomeScreenUI(this@MainActivity, navController, homeScreenState) }
                composable("scene_description") { sceneDescription.SceneDescriptionUI(this@MainActivity, navController) }
                composable("screen3") { screen3.Screen3UI(this@MainActivity, navController) }
            }
        }
    }
}