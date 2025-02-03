package com.swarn.terminalapp.domain.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class Repository(
    val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend inline fun <reified T> getResult(
        noinline function: suspend () -> T,
    ): T {
        return withContext(defaultDispatcher) {
            function.invoke()
        }
    }
}
