package location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import model.PermissionResult
import model.RequestCallBack
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import kotlin.concurrent.AtomicReference

actual class LocationRequester {
    private val locationManager = CLLocationManager()
    private lateinit var locationRequest: RequestCallBack

    @Composable
    actual fun registerLocationRequest(
        onAllowed: () -> Unit,
        onDenied: () -> Unit,
    ) {
        if (!::locationRequest.isInitialized) {
            locationRequest = RequestCallBack(
                onNegative = onDenied,
                onPositive = onAllowed
            )
        }
    }


    @Composable
    actual fun rememberLocationPermissionResult(
    ): PermissionResult {
        return remember {
            locationManager.authorizationStatus().mapToAppPermission()
        }
    }

    actual fun launchLocationRequest() {
        locationManager.requestAlwaysAuthorization()
        val permissionResult =
            locationManager.authorizationStatus().mapToAppPermission()
        if (permissionResult.isUnknown) {
            locationManager.requestAlwaysAuthorization()
        } else if (permissionResult.isGranted) {
            locationRequest.onPositive.invoke()
        } else {
            locationRequest.onNegative.invoke()
        }
    }


}


fun CLAuthorizationStatus.mapToAppPermission(): PermissionResult {
    return when (this) {
        kCLAuthorizationStatusAuthorizedAlways,
        kCLAuthorizationStatusAuthorizedWhenInUse,
        kCLAuthorizationStatusRestricted
        -> PermissionResult.GRANTED

        kCLAuthorizationStatusNotDetermined -> PermissionResult.UNKNOWN
        kCLAuthorizationStatusDenied -> PermissionResult.DENIED
        else -> PermissionResult.UNKNOWN
    }

}