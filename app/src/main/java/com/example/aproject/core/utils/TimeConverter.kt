package com.example.aproject.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Formats this epoch-milliseconds timestamp as a localized date-time string
 * using the device's locale and time zone.
 *
 * Example: `1700000000000L.toLocalizedDateTime()` → "3 июл. 2026 г., 15:30:45".
 */
fun Long.toLocalizedDateTime(): String {
    val dateInstant = Instant.ofEpochMilli(this)
    val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withZone(ZoneId.systemDefault()) //Sets the time zone of the user's device.
    val formattedDate = dateFormatter.format(dateInstant)
    return formattedDate
}