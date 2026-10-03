package com.example.aproject.feature.advice.domain.model

/**
 * Domain model of an advice, used by the UI.
 */
data class Advice(val id: Long = 0, val advice: String, val timeCreation: Long = System.currentTimeMillis())
