package location

import android.content.Context
import android.os.Looper

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.AppLocation
import model.DispatchersProvider
import util.AppError
import util.AppResult
import util.LocationError
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class LocationProvider(
    private val dispatchers: DispatchersProvider,
    private val context: Context
) {
    companion object {
        const val LOCATION_PRIORITY = Priority.PRIORITY_BALANCED_POWER_ACCURACY

        const val CHECKING_INTERVAl = 500L

        const val MIN_DISTANCE = 100f
    }

    private val client by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @Suppress("missingPermission")
    actual suspend fun getCurrentLocation(): AppResult<AppLocation, AppError> {
        return withContext(dispatchers.io) {
            suspendCoroutine { cont ->
                client.lastLocation.addOnSuccessListener { loc ->
                    loc?.let {
                        cont.resume(
                            AppResult.Done(
                                AppLocation(
                                    lat = loc.latitude,
                                    lng = loc.longitude
                                )
                            )
                        )
                    } ?: run {
                        cont.resume(AppResult.Error(LocationError.UNKNOWN))
                    }
                }.addOnFailureListener {
                    cont.resume(AppResult.Error(LocationError.UNKNOWN))
                }
            }

        }
    }

    @Suppress("missingPermission")
    actual fun listenToLocationUpdates(): Flow<AppLocation> = callbackFlow<AppLocation> {
        val locationRequest = LocationRequest.Builder(
            LOCATION_PRIORITY,
            CHECKING_INTERVAl,
        ).setMinUpdateDistanceMeters(MIN_DISTANCE).build()
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { lastLocation ->
                    val location = AppLocation(
                        lat = lastLocation.latitude,
                        lng = lastLocation.longitude
                    )
                    trySend(location)
                }
            }

        }
        client.requestLocationUpdates(
            locationRequest,
            locationCallBack,
            Looper.getMainLooper()
        )
        awaitClose {
            client.removeLocationUpdates(locationCallBack)
        }
    }.flowOn(dispatchers.io)


}
