package com.swarn.terminalapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swarn.terminalapp.domain.model.TransactionType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.math.RoundingMode

@RunWith(AndroidJUnit4::class)
class TransactionsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var transactionsDao: TransactionsDao

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        transactionsDao = appDatabase.transactionsDao()

        // Create the trigger manually
        val db = appDatabase.openHelper.writableDatabase

        // SQL to create the trigger
        val createTriggerSQL = """
            CREATE TRIGGER update_balance_after_transaction
            AFTER INSERT ON transactions
            FOR EACH ROW
            BEGIN
                -- Update the balance based on the transaction type
                UPDATE balance
                SET totalBalance = ROUND(
                    totalBalance + 
                    (CASE 
                        WHEN NEW.transactionType = 'CREDIT' THEN NEW.amount
                        ELSE -NEW.amount
                    END), 2)
                WHERE id = 1;
            END;
        """
        db.execSQL(createTriggerSQL)

        // Insert initial balance if not already present
        val insertInitialBalanceSQL = """
            INSERT INTO balance (id, totalBalance)
            SELECT 1, 0
            WHERE NOT EXISTS (SELECT 1 FROM balance WHERE id = 1);
        """
        db.execSQL(insertInitialBalanceSQL)
    }

    @Test
    fun testInsertTransaction() = runBlocking {
        val creditTransaction =
            Transactions(amount = BigDecimal(100.0), transactionType = TransactionType.CREDIT)
        transactionsDao.insertTransaction(creditTransaction)

        val allTransactions = transactionsDao.getAllTransactions()
        assertEquals(1, allTransactions.size)
        assertEquals(BigDecimal(100.0), allTransactions[0].amount)
        assertEquals(TransactionType.CREDIT, allTransactions[0].transactionType)
    }

    @Test
    fun testGetTotalBalance() = runBlocking {
        val amount = 100.toBigDecimal().setScale(1, RoundingMode.HALF_EVEN)
        val initialBalance = transactionsDao.getTotalBalance()
        assertEquals(BigDecimal(0.0), initialBalance)

        val creditTransaction =
            Transactions(amount = amount, transactionType = TransactionType.CREDIT)
        transactionsDao.insertTransaction(creditTransaction)

        val balanceAfterCredit = transactionsDao.getTotalBalance()
        assertEquals(amount, balanceAfterCredit)
    }

    @Test
    fun testGetAllTransactions() = runBlocking {
        val creditTransaction =
            Transactions(amount = BigDecimal(100.0), transactionType = TransactionType.CREDIT)
        val debitTransaction =
            Transactions(amount = BigDecimal(50.0), transactionType = TransactionType.DEBIT)
        transactionsDao.insertTransaction(creditTransaction)
        transactionsDao.insertTransaction(debitTransaction)

        val allTransactions = transactionsDao.getAllTransactions()
        assertEquals(2, allTransactions.size)

        assertEquals(BigDecimal(100.0), allTransactions[0].amount)
        assertEquals(TransactionType.CREDIT, allTransactions[0].transactionType)
        assertEquals(BigDecimal(50.0), allTransactions[1].amount)
        assertEquals(TransactionType.DEBIT, allTransactions[1].transactionType)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }
}
