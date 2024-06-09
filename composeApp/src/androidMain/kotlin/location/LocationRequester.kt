package location

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import model.PermissionResult
import org.mohaberabi.rpos.hasLocationPermission
import org.mohaberabi.rpos.shouldShowLocationPermissionRationale

actual class LocationRequester {

    private lateinit var getMultipleContent: ActivityResultLauncher<Array<String>>

    @Composable
    actual fun registerLocationRequest(
        onAllowed: () -> Unit,
        onDenied: () -> Unit,
    ) {
        getMultipleContent =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
            ) { perms ->
                if (perms.values.any { true }) {
                    onAllowed()
                } else {
                    onDenied()
                }

            }

    }


    @Composable
    actual fun rememberLocationPermissionResult(): PermissionResult {
        val context = LocalContext.current as ComponentActivity
        val activity = LocalContext.current as ComponentActivity
        val showRational = activity.shouldShowLocationPermissionRationale()
        val allowedLocation = context.hasLocationPermission()
        val res = if (allowedLocation) {
            PermissionResult.GRANTED
        } else if (showRational) {
            PermissionResult.UNKNOWN
        } else {
            PermissionResult.DENIED
        }
        return remember { res }
    }

    actual fun launchLocationRequest(
    ) {
        getMultipleContent.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}