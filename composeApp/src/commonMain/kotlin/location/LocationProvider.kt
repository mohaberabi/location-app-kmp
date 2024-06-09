package location

import kotlinx.coroutines.flow.Flow
import model.AppLocation
import util.AppError
import util.AppResult

expect class LocationProvider {
    suspend fun getCurrentLocation(): AppResult<AppLocation, AppError>


    fun listenToLocationUpdates(): Flow<AppLocation>
}

