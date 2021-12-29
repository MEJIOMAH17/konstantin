package com.github.mejiomah17.konstantin.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.github.mejiomah17.konstantin.icons.konstantinicons.Lightbulb
import kotlin.collections.List as ____KtList

public object KonstantinIcons

private var __AllAssets: ____KtList<ImageVector>? = null

public val KonstantinIcons.AllAssets: ____KtList<ImageVector>
    get() {
        if (__AllAssets != null) {
            return __AllAssets!!
        }
        __AllAssets = listOf(Lightbulb)
        return __AllAssets!!
    }
