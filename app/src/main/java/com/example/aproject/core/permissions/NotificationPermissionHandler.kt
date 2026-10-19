package com.example.aproject.core.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Composable that requests the POST_NOTIFICATIONS permission on Android 13+.
 *
 * It checks if the permission is already granted and whether the user has been asked before
 * (stored in [DataStore]). If not, it triggers the system permission dialog.
 *
 * Once the user responds, the "has asked" flag is saved to [DataStore] to avoid showing
 * the dialog again on subsequent launches.
 */
@Composable
fun NotificationPermissionHandler(
    dataStore: DataStore<Preferences>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val hasAskedKey = remember { booleanPreferencesKey("has_asked_notification_permission") }

    // Read the "has asked" flag from DataStore
    val hasAskedFlow = remember(dataStore) {
        dataStore.data.map { preferences ->
            preferences[hasAskedKey] ?: false
        }
    }
    val hasAsked: Boolean by hasAskedFlow.collectAsState(initial = true)

    // Check if the permission is currently granted
    val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // On Android < 13 the permission is not needed, it is true by default
    }

    // Create a launcher for the system permission dialog
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // This callback is triggered when the user clicks "Allow" or "Deny"
        scope.launch {
            dataStore.edit { it[hasAskedKey] = true }
        }
    }

    // 4. Trigger the system dialog if needed
    LaunchedEffect(hasPermission, hasAsked) {
        if (!hasPermission && !hasAsked) {
            // Launch the system permission dialog
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}