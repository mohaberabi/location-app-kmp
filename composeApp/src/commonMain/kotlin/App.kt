import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import location.LocationRequester
import di.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import viewmodel.LocationState
import viewmodel.LocationViewModel

object KoinGraph : KoinComponent {
    val requester: LocationRequester by inject()
}

@Composable
fun App() {
    val viewmodel: LocationViewModel = koinViewModel()
    val state by viewmodel.state.collectAsState()
    val requester = KoinGraph.requester
    MaterialTheme {
        HomeScreen(
            state = state,
            locationRequester = requester,
        )
    }
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: LocationState,
    locationRequester: LocationRequester,
) {

    locationRequester.registerLocationRequest(
        onAllowed = {

        },

        onDenied = {

        },
    )
    LaunchedEffect(Unit) {
        locationRequester.launchLocationRequest()
    }

    when (state) {
        is LocationState.Done -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Your Current Location")
                Text("Lat: ${state.location.lat}")
                Text("Lng: ${state.location.lng}")
            }
        }

        LocationState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Please Allow Access To Location")
            }
        }

        else -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

}