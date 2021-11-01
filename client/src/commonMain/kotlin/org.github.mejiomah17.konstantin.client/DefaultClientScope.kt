package org.github.mejiomah17.konstantin.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

internal expect object DefaultClientScope : CoroutineScope