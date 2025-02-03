package com.swarn.terminalapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.swarn.terminalapp.data.local.AppDatabase
import com.swarn.terminalapp.data.local.TransactionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "transactions_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // Insert initial balance into the balance table if it's empty
                val insertInitialBalanceSQL = """
                    INSERT INTO balance (id, totalBalance) 
                    SELECT 1, 0 
                    WHERE NOT EXISTS (SELECT 1 FROM balance WHERE id = 1)
                """
                db.execSQL(insertInitialBalanceSQL)

                // SQL to create the trigger to automatically update balance
                val createTriggerSQL = """
                    CREATE TRIGGER update_balance_after_transaction
                    AFTER INSERT ON transactions
                    FOR EACH ROW
                    BEGIN
                        -- Update balance based on the transaction type with rounding
                        UPDATE balance
                        SET totalBalance = ROUND(
                            totalBalance + 
                            (CASE 
                                WHEN NEW.transactionType = 'CREDIT' THEN NEW.amount -- CREDIT
                                ELSE -NEW.amount                                  -- DEBIT
                            END),
                            2 -- Round to 2 decimal places
                        )
                        WHERE id = 1;
                    END;
                """

                // Execute the SQL to create the trigger
                db.execSQL(createTriggerSQL)
            }
        }).allowMainThreadQueries()
            .build()
    }


    @Provides
    @Singleton
    fun provideTransactionsDao(appDatabase: AppDatabase): TransactionsDao {
        return appDatabase.transactionsDao()
    }
}