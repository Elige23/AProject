package com.example.aproject.core.di.qualifiers

import javax.inject.Qualifier

/**
 * Qualifier for the `Retrofit` instance of the Advice API.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AdviceApiRetrofit