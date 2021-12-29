package org.github.mejiomah17.konstantin.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

internal actual object DefaultClientScope : CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy {
        (SupervisorJob() + Executors.newFixedThreadPool(4).asCoroutineDispatcher())
    }
}
