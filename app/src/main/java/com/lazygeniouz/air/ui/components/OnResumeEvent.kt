package com.lazygeniouz.air.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Invokes [onResumeEvent] everytime the [Lifecycle] hits [Lifecycle.Event.ON_RESUME] to update
 * certain values or fields.
 */
@Composable
fun OnResumeEvent(onResumeEvent: () -> Unit) {
    val eventHandler = rememberUpdatedState(onResumeEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) eventHandler.value()
        }

        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}