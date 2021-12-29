package com.github.mejiomah17.konstantin.configuration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface StateChannelFactory<S> : (CoroutineScope) -> ReceiveChannel<S>
