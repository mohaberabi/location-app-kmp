package org.mohaberabi.rpos

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat


fun ComponentActivity.shouldShowLocationPermissionRationale(): Boolean =
    shouldShowRequestPermissionRationale(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )


fun Context.hasPermission(
    permission: String,
): Boolean = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

fun Context.hasLocationPermission(): Boolean =
    hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            || hasPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)

