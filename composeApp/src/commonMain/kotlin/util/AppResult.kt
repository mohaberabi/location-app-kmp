package util

sealed interface AppResult<out D, out E : AppError> {
    data class Done<out D>(val data: D) : AppResult<D, Nothing>
    data class Error<out E : AppError>(val error: E) : AppResult<Nothing, E>
}


interface AppError
enum class LocationError : AppError {
    UNKNOWN
}