package di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import model.DefaultDispatcherProvider
import model.DispatchersProvider
import org.koin.compose.currentKoinScope
import org.koin.dsl.module


val appModule = module {

    single<DispatchersProvider> { DefaultDispatcherProvider() }


}


@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}