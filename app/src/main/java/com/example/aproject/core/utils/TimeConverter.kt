package com.example.aproject.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


fun Long.toLocalizedDateTime(): String {
    val dateInstant = Instant.ofEpochMilli(this)
    val dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withZone(ZoneId.systemDefault()) //systemDefault() использует язык/регион, установленные пользователем на телефоне.
    val formattedDate = dateFormatter.format(dateInstant)
    return formattedDate
}

