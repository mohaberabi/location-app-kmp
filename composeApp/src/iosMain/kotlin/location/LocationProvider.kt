package location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import model.AppLocation
import model.DispatchersProvider
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLDistanceFilterNone
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import util.AppError
import util.AppResult
import util.LocationError
import kotlin.concurrent.AtomicReference
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class LocationProvider(
    private val dispatchers: DispatchersProvider,
) {
    companion object {
        private val ACCURACY = kCLLocationAccuracyBest
        private val DISTANCE_FILTER = kCLDistanceFilterNone
    }

    private val locationManager: CLLocationManager = CLLocationManager()
    private val locationDelegate = LocationDelegate()

    private class LocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
        var onLocationUpdate: ((AppLocation?) -> Unit)? = null
        override fun locationManager(
            manager: CLLocationManager,
            didUpdateLocations: List<*>
        ) {
            didUpdateLocations.firstOrNull()?.let {
                val location = it as CLLocation
                onLocationUpdate?.invoke(location.toAppLocation())
            }
        }


        override fun locationManager(
            manager: CLLocationManager,
            didFailWithError: NSError
        ) {
            onLocationUpdate?.invoke(null)
        }
    }


    actual fun listenToLocationUpdates(): Flow<AppLocation> = callbackFlow {
        initializeLocationManager()
        locationDelegate.onLocationUpdate = { location ->
            if (location != null) {
                trySend(location).isSuccess
            }
        }

        locationManager.delegate = locationDelegate
        locationManager.startUpdatingLocation()
        awaitClose {
            locationManager.stopUpdatingLocation()
            locationManager.delegate = null
        }
    }.flowOn(dispatchers.io)


    actual suspend fun getCurrentLocation(): AppResult<AppLocation, AppError> {
        return try {
            val cllLocation = locationManager.location
            cllLocation?.let {
                AppResult.Done(it.toAppLocation())
            } ?: AppResult.Error(LocationError.UNKNOWN)
        } catch (e: Exception) {
            AppResult.Error(LocationError.UNKNOWN)
        }

    }


    private fun initializeLocationManager() {
        locationManager.requestWhenInUseAuthorization()
        locationManager.desiredAccuracy = ACCURACY
        locationManager.distanceFilter = DISTANCE_FILTER
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun CLLocation.toAppLocation(): AppLocation {
    return coordinate.useContents {
        AppLocation(
            latitude,
            longitude,
        )
    }

}
