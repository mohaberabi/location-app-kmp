package custom

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import viewmodel.LocationViewModel


actual val viewModelModule = module {

    viewModelOf(::LocationViewModel)
}