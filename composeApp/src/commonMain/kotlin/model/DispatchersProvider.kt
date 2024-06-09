package model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

interface DispatchersProvider {


    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val unconfiend: CoroutineDispatcher
    val default: CoroutineDispatcher
}


class DefaultDispatcherProvider() : DispatchersProvider {

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val unconfiend: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val default: CoroutineDispatcher
        get() = Dispatchers.Default

}