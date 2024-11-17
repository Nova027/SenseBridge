package com.example.sensebridge

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