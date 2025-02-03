package com.swarn.terminalapp.di

import com.swarn.terminalapp.data.local.TransactionLocalDataSource
import com.swarn.terminalapp.data.local.TransactionsDao
import com.swarn.terminalapp.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(transactionsDao: TransactionsDao): TransactionLocalDataSource {
        return TransactionLocalDataSource(transactionsDao)
    }

    @Provides
    @Singleton
    fun provideRepository(
        transactionLocalDataSource: TransactionLocalDataSource,
    ): TransactionRepository {
        return TransactionRepository(transactionLocalDataSource)
    }
}
