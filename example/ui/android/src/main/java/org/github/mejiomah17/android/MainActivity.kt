package org.github.mejiomah17.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import org.github.mejiomah17.konstantin.api.App

class MainActivity : AppCompatActivity() {
    @ExperimentalGraphicsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            App()
        }
    }
}