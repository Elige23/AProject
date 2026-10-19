package com.example.aproject.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Creates a [DataStore] instance for storing user preferences as key-value pairs.
 *
 * This is a property delegate that ensures only one [DataStore] instance is created
 * per file name for the entire application.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")