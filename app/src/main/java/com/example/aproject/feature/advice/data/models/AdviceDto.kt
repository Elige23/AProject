package com.example.aproject.feature.advice.data.models

import com.google.gson.annotations.SerializedName

//Если мы в браузере введём https://api.adviceslip.com/advice, то получим такой ответ
//{"slip": { "id": 133, "advice": "If you find yourself distressed about something, ask yourself if it will still matter tomorrow or next week or next month."}}

// DTO для одного совета из API
data class AdviceDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("advice")
    val advice: String
)

// DTO для обертки ответа
data class AdviceResponse(
    @SerializedName("slip")
    val slip: AdviceDto
)
