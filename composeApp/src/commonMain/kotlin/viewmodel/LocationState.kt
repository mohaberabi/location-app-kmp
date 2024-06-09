package viewmodel

import model.AppLocation


sealed interface LocationState {

    data object Loading : LocationState
    data object Error : LocationState
    data class Done(val location: AppLocation) : LocationState
}