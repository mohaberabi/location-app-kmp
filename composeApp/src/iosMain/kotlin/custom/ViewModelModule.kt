package custom

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import viewmodel.LocationViewModel


actual val viewModelModule = module {
    factoryOf(::LocationViewModel)
}
