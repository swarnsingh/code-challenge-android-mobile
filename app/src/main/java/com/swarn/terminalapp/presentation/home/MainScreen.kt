package com.swarn.terminalapp.presentation.home

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swarn.terminalapp.R
import com.swarn.terminalapp.data.ResultState
import com.swarn.terminalapp.designsystem.composables.ComposePreviewParameterConfig
import com.swarn.terminalapp.designsystem.composables.DSScreen
import com.swarn.terminalapp.designsystem.composables.UIModePreviews
import com.swarn.terminalapp.designsystem.composables.appbar.DSAppBar
import com.swarn.terminalapp.designsystem.composables.input.AmountInput
import com.swarn.terminalapp.designsystem.composables.progressbar.LoadingOverlay
import com.swarn.terminalapp.designsystem.composables.theme.DSAppTheme
import com.swarn.terminalapp.designsystem.composables.theme.DSColor
import com.swarn.terminalapp.designsystem.composables.theme.DSShapes
import com.swarn.terminalapp.domain.model.Transaction
import com.swarn.terminalapp.domain.model.TransactionType
import com.swarn.terminalapp.util.formatAmount
import java.math.BigDecimal

@Composable
fun MainScreen(
    transactionState: ResultState<Transaction>,
    onDepositClick: (Double) -> Unit,
    onWithdrawClick: (Double) -> Unit,
    onHistoryClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val isLoading = transactionState is ResultState.Loading
    val context = LocalContext.current
    val shouldSnackbarShown = rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(transactionState) {
        val msg = getTransactionMessage(context, transactionState)
        if (msg.isNotEmpty() && shouldSnackbarShown.value) {
            snackbarHostState.showSnackbar(
                message = msg,
                actionLabel = context.getString(R.string.dismiss),
                duration = SnackbarDuration.Short
            )
        }
        if (!shouldSnackbarShown.value) {
            shouldSnackbarShown.value = true
        }
    }

    DSScreen(
        adjustResizeKeyboardBehavior = true,
        topBar = {
            DSAppBar(stringResource(id = R.string.app_name))
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            MainScreenContent(
                paddingValues = paddingValues,
                textState = textState,
                onDepositClick = { amount ->
                    onDepositClick(amount)
                    textState.value = TextFieldValue("")
                },
                onWithdrawClick = { amount ->
                    onWithdrawClick(amount)
                    textState.value = TextFieldValue("")
                },
                onHistoryClick = {
                    onHistoryClick.invoke()
                    shouldSnackbarShown.value = false
                },
                totalBalance = transactionState.data?.totalBalance ?: 0.0.toBigDecimal()
            )
            LoadingOverlay(isLoading = isLoading)
        }
    }
}

@Composable
fun MainScreenContent(
    paddingValues: PaddingValues,
    textState: MutableState<TextFieldValue>,
    onDepositClick: (Double) -> Unit,
    onWithdrawClick: (Double) -> Unit,
    onHistoryClick: () -> Unit,
    totalBalance: BigDecimal,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AmountInput(
                modifier = Modifier
                    .height(128.dp),
                value = textState.value,
                onValueChange = { textState.value = TextFieldValue(it) },
                textStyle = TextStyle(fontSize = 48.sp, textAlign = TextAlign.Center),
            )
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = totalBalance.toString(),
                style = TextStyle(fontSize = 24.sp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                enabled = !textState.value.text.isEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("DepositButton"),
                onClick = {
                    val amount = textState.value.text.toDoubleOrNull()
                    amount?.let { onDepositClick(formatAmount(it)) }
                },
                shape = DSShapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DSColor.ButtonColor,
                )
            ) {
                Text(stringResource(id = R.string.deposit).toUpperCase(Locale.current))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = !textState.value.text.isEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("WithdrawButton"),
                onClick = {
                    val amount = textState.value.text.toDoubleOrNull()
                    amount?.let { onWithdrawClick(formatAmount(it)) }
                },
                shape = DSShapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DSColor.ButtonColor,
                )
            ) {
                Text(stringResource(id = R.string.withdraw).toUpperCase(Locale.current))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("HistoryButton"),
                onClick = onHistoryClick,
                shape = DSShapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DSColor.ButtonColor,
                )
            ) {
                Text(stringResource(id = R.string.history).toUpperCase(Locale.current))
            }
        }
    }
}

fun getTransactionMessage(context: Context, transactionState: ResultState<Transaction>): String {
    return when (transactionState) {
        is ResultState.Success -> {
            if (transactionState.data.isTransactionSuccessful) {
                when (transactionState.data.transactionType) {
                    TransactionType.DEBIT -> context.getString(R.string.amount_debited_success_msg)
                    TransactionType.CREDIT -> context.getString(R.string.amount_credited_success_msg)
                    else -> ""
                }
            } else {
                when (transactionState.data.transactionType) {
                    TransactionType.DEBIT -> context.getString(R.string.not_enough_balance)
                    TransactionType.CREDIT -> context.getString(R.string.transaction_failed)
                    TransactionType.CHECK_BALANCE -> context.getString(R.string.check_balance_failed)
                    else -> ""
                }
            }
        }

        is ResultState.Error -> {
            context.getString(
                R.string.error,
                transactionState.throwable.localizedMessage
            )
        }

        else -> ""
    }
}

@Composable
@UIModePreviews
private fun MainScreenPreview(
    @PreviewParameter(MainScreenPreviewProvider::class) previewConfig: ComposePreviewParameterConfig<ResultState<Transaction>>,
) {
    DSAppTheme {
        MainScreen(
            transactionState = previewConfig.value(),
            onDepositClick = {},
            onWithdrawClick = {},
            onHistoryClick = {},
        )
    }
}