package com.swarn.terminalapp.presentation.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swarn.terminalapp.designsystem.composables.NormalModePreviews
import com.swarn.terminalapp.designsystem.composables.theme.DSAppTheme
import com.swarn.terminalapp.designsystem.composables.theme.DSColor
import com.swarn.terminalapp.designsystem.composables.theme.Typography
import com.swarn.terminalapp.domain.model.TransactionHistory
import com.swarn.terminalapp.domain.model.TransactionType
import kotlin.toBigDecimal

const val DR = "DR"
const val CR = "CR"

@Composable
internal fun TransactionHistoryCardView(
    modifier: Modifier = Modifier,
    transaction: TransactionHistory,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon: ImageVector
        val iconColor: Color
        val typeLabel: String
        val labelColor: Color

        when (transaction.transactionType) {
            TransactionType.CREDIT -> {
                icon = Icons.Default.ArrowDownward
                iconColor = Color.Green
                typeLabel = CR
                labelColor = Color.DarkGray
            }

            TransactionType.DEBIT -> {
                icon = Icons.Default.ArrowUpward
                iconColor = Color.Red
                typeLabel = DR
                labelColor = Color.Gray
            }

            else -> {
                icon = Icons.Default.Sync
                iconColor = Color.Gray
                typeLabel = ""
                labelColor = Color.Gray
            }
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.dateAndTime,
                style = Typography.bodyLarge.copy(color = DSColor.Purple700)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = transaction.amount.toString(),
                style = Typography.bodyLarge.copy(color = DSColor.Purple700)
            )
            Text(
                text = typeLabel,
                style = Typography.bodyMedium.copy(
                    color = labelColor,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
@NormalModePreviews
private fun TransactionHistoryCardViewPreview() {
    DSAppTheme {
        TransactionHistoryCardView(
            transaction = TransactionHistory(
                id = 1,
                amount = 10.0.toBigDecimal(),
                transactionType = TransactionType.DEBIT,
                dateAndTime = "2024-05-04"
            )
        )
    }
}
