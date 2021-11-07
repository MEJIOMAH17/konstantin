package org.github.mejiomah17.konstantin.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext
//TODO I do not understand js concurrency! Rewrite this code is MUST before release!
internal actual object DefaultClientScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = GlobalScope.coroutineContext
}