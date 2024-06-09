package custom

import android.content.Context
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


actual class KoinInit(
    private val context: Context,
) {
    actual fun initialize() {
        startKoin {
            androidContext(context)
            androidLogger()
            modules(
                appModule,
                locationModule,
                viewModelModule,
            )
        }
    }
}