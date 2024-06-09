package custom

import location.LocationRequester
import location.LocationProvider
import org.koin.dsl.module


actual val locationModule = module {


    single<LocationProvider> { LocationProvider(get()) }

    single<LocationRequester> {
        LocationRequester()
    }

}