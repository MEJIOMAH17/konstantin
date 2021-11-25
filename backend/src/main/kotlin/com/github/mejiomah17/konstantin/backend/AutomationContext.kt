package com.github.mejiomah17.konstantin.backend

import kotlinx.coroutines.CoroutineScope

interface AutomationContext {
    val stateManager: StateManager
    val coroutineScope: CoroutineScope
}