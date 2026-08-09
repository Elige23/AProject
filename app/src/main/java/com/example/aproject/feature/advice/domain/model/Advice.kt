package com.example.aproject.feature.advice.domain.model


data class Advice(val id: Long = 0, val advice: String, val timeCreation: Long = System.currentTimeMillis())
