package location

import androidx.compose.runtime.Composable
import model.PermissionResult


expect class LocationRequester {
    @Composable
    fun registerLocationRequest(
        onAllowed: () -> Unit,
        onDenied: () -> Unit,
    )


    fun launchLocationRequest()

    @Composable
    fun rememberLocationPermissionResult(): PermissionResult
}


