package model

enum class PermissionResult {
    DENIED,
    GRANTED,
    UNKNOWN;

    val isDenied: Boolean
        get() = this == DENIED
    val isUnknown: Boolean
        get() = this == UNKNOWN
    val isGranted: Boolean
        get() = this == GRANTED
}

