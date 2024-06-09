package custom

import di.appModule
import org.koin.core.context.startKoin


actual class KoinInit(
) {
    actual fun initialize() {
        startKoin {
            modules(
                appModule,
                locationModule,
                viewModelModule,
            )
        }
    }
}