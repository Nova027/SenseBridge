package com.example.sensebridge

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState

data class FeatureInfo(
    val name: String,
    val todo: Int
    // To add more members, including next screen to Navigate
)

val featuresAvailable = listOf(
        FeatureInfo("Voice Assistant", 1),
        FeatureInfo("Chatbot", 1),
        FeatureInfo("Scene Description", 1)
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