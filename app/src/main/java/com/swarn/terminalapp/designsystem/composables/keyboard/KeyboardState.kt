package com.swarn.terminalapp.designsystem.composables.keyboard

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class KeyboardStateData(val visible: Boolean, val height: Int)

class KeyboardState {
    companion object {
        private val _keyboardStateFlow = MutableStateFlow(KeyboardStateData(false, 0))
        val keyboardStateFlow = _keyboardStateFlow.asStateFlow()

        fun update(visible: Boolean, height: Int) {
            _keyboardStateFlow.value = KeyboardStateData(visible, height)
        }
    }
}
