package com.example.sensebridge

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat

// Data class to hold information about an available feature
data class FeatureInfo(
    val name: String,
    val todo: Boolean,
    val nextScreen: String = "",
    val requiredPermissions: List<String> = listOf()
)

// Check if all required permissions are granted for given feature
fun checkPermissionsGranted(featureInfo: FeatureInfo, mainContext: Context) : List<String> {
    var missingPermissions = mutableListOf<String>()
    for (permission in featureInfo.requiredPermissions) {
        if (ContextCompat.checkSelfPermission(mainContext.applicationContext, permission) != PackageManager.PERMISSION_GRANTED)
            missingPermissions += permission
    }
    return missingPermissions
}

data class SessionData (
    var feature : FeatureInfo,
    var index : Int
)

// Data class to hold state variables required for Home screen UI
data class HomeScreenState (
    var showAddSessionDialog : MutableState<Boolean>,
    var voiceButtonPressed : MutableState<Boolean>,
    var sessionCount : MutableIntState,
    var selectedFeature : MutableState<FeatureInfo>,
    var isFeatureSelected : MutableState<Boolean>
)

// Sealed class to hold possible input formats
sealed class InputFormat {

}

val featuresAvailable = listOf(
    FeatureInfo("Voice Assistant", true),
    FeatureInfo("Chatbot", false, "screen3"),
    FeatureInfo("Scene Description", false, "scene_description",
        listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO))
)

val sessionList : MutableList<SessionData> = mutableListOf()

val permissionList = arrayOf(
    android.Manifest.permission.RECORD_AUDIO,
    android.Manifest.permission.CAMERA
)