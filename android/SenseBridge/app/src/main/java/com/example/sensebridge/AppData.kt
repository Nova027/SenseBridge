package com.example.sensebridge

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState

data class FeatureInfo(
    val name: String,
    val todo: Boolean,
    val nextScreen: String = ""
    // To add more members, including next screen to Navigate
)

data class SessionData (
    var feature : FeatureInfo,
    var index : Int
)

data class HomeScreenState (
    var showAddSessionDialog : MutableState<Boolean>,
    var voiceButtonPressed : MutableState<Boolean>,
    var sessionCount : MutableIntState,
    var selectedFeature : MutableState<FeatureInfo>,
    var isFeatureSelected : MutableState<Boolean>
)

val featuresAvailable = listOf(
    FeatureInfo("Voice Assistant", true),
    FeatureInfo("Chatbot", false, "screen3"),
    FeatureInfo("Scene Description", false, "scene_description")
)

val sessionList : MutableList<SessionData> = mutableListOf()

val permissionList = arrayOf(
    android.Manifest.permission.RECORD_AUDIO,
    android.Manifest.permission.CAMERA
)