package com.swarn.terminalapp.data

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Sealed class representing the state of a result.
 * This class is used to represent the different states (Success, Loading, Error)
 * of an operation that produces a result of type [T].
 *
 * @param T the type of data the result holds.
 * @param data the data that is associated with the result. It can be null in some states.
 */
@Immutable
sealed class ResultState<T>(open val data: T?) {
    class Success<T>(override val data: T) : ResultState<T>(data)
    class Loading<T>(override val data: T? = null) : ResultState<T>(data)
    class Error<T>(override val data: T? = null, val throwable: Throwable) : ResultState<T>(data)
}

/**
 * Extension function for `MutableStateFlow` to fetch data and emit loading, success,
 * or error states in a suspendable and inlineable manner.
 *
 * @param T The type of data encapsulated in the `ResultState`.
 * @param block A `suspend` lambda function representing the operation to be performed,
 *              such as a network or database call. It is marked as `noinline` because
 *              suspend functions inherently cannot be inlined due to their coroutine
 *              context requirements.
 *
 * This function emits:
 * - `ResultState.Loading` while the operation is in progress.
 * - `ResultState.Success` if the operation completes successfully.
 * - `ResultState.Error` if an exception occurs during the operation.
 *
 * Usage:
 * ```
 * val stateFlow = MutableStateFlow<ResultState<T>>(ResultState.Loading())
 * stateFlow.fetchResponse { someSuspendFunction() }
 * ```
 */
suspend inline fun <T> MutableStateFlow<ResultState<T>>.fetchResponse(
    noinline block: suspend () -> T,
) {
    try {
        emit(ResultState.Loading(value.data))
        emit(ResultState.Success(block()))
    } catch (e: Throwable) {
        emit(ResultState.Error(value.data, e))
    }
}
