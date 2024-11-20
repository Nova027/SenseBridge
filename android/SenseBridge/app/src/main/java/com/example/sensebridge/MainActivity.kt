package com.example.sensebridge

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity()
{
    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantMap ->
        for ((permission, granted) in grantMap) {
            if (granted)
                Log.d("MainActivity", "Permission $permission granted!")
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                Toast.makeText(this, "Permission ${permission.split('.')[2]} denied!", Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "Permission ${permission.split('.')[2]} denied! Please grant it in settings", Toast.LENGTH_SHORT).show()
                // Close the app
                finish()
                // Open app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Launch async request for permissions, automatically checks if already granted previously,
        // and avoids showing dialogs again. Also avoids race conditions between multiple permissions handling!
        // The processing within lambda happens only after user-input (if applicable) is provided through the dialog
        requestPermissionLauncher.launch(permissionList)
        setContent {
            val homeScreenState = HomeScreenState(
                showAddSessionDialog = remember { mutableStateOf(false) },
                voiceButtonPressed = remember { mutableStateOf(false) },
                sessionCount = remember { mutableIntStateOf(0) },
                selectedFeature = remember { mutableStateOf(FeatureInfo("Invalid", false)) },
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