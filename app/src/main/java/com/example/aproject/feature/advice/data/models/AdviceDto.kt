package com.example.aproject.feature.advice.data.models

import com.google.gson.annotations.SerializedName

/**
 * DTO for parsing single advice from the Advice Slip API.
 */
data class AdviceDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("advice")
    val advice: String
)

/**
 * Wrapper for the Advice Slip API response.
 *
 * The API returns the advice nested under the `slip` key:
 * ```
 * { "slip": { "id": 133, "advice": "..." } }
 * { "slip": { "id": 133, "advice": "If you find yourself distressed about something, ask yourself if it will still matter tomorrow."}}
 * ```
 * @see <a href="https://api.adviceslip.com/advice">Advice Slip</a>
 */
data class AdviceResponse(
    @SerializedName("slip")
    val slip: AdviceDto
)
