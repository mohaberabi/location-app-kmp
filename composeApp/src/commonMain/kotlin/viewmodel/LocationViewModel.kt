package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import location.LocationProvider
import util.AppResult


class LocationViewModel(
    private val locationProvider: LocationProvider,
) : ViewModel() {


    val state: StateFlow<LocationState> = locationProvider.listenToLocationUpdates()
        .map {
            LocationState.Done(it) as LocationState
        }.onStart {
            emit(LocationState.Loading)
        }
        .catch {
            emit(LocationState.Error)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            LocationState.Loading
        )


}