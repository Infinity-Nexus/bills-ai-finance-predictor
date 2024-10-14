package com.commercial.bills.di

import android.app.Application
import androidx.work.WorkManager
import com.commercial.bills.data.dao.BalanceDao
import com.commercial.bills.data.dao.LastTransactionDao
import com.commercial.bills.data.dao.RecurringTransactionDao
import com.commercial.bills.data.dao.SavingPlanDao
import com.commercial.bills.data.database.AppDatabase
import com.commercial.bills.data.repository.BalanceRepository
import com.commercial.bills.data.repository.LastTransactionRepository
import com.commercial.bills.data.repository.RecurringTransactionRepository
import com.commercial.bills.data.repository.SavingPlanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application) =
        AppDatabase.getInstance(application)

    @Provides
    @Singleton
    fun provideBalanceDao(appDatabase: AppDatabase) =
        appDatabase.balanceDao()

    @Provides
    @Singleton
    fun provideLastTransactionDao(appDatabase: AppDatabase) =
        appDatabase.lastTransactionDao()

    @Provides
    @Singleton
    fun provideRecurringTransactionDao(appDatabase: AppDatabase) =
        appDatabase.recurringTransactionDao()

    @Provides
    @Singleton
    fun provideSavingPlanDao(appDatabase: AppDatabase) =
        appDatabase.savingPlanDao()

    @Provides
    @Singleton
    fun provideBalanceRepository(balanceDao: BalanceDao) =
        BalanceRepository(balanceDao = balanceDao)

    @Provides
    @Singleton
    fun provideLastTransactionRepository(lastTransactionDao: LastTransactionDao) =
        LastTransactionRepository(lastTransactionDao = lastTransactionDao)

    @Provides
    @Singleton
    fun provideRecurringTransactionRepository(recurringTransactionDao: RecurringTransactionDao) =
        RecurringTransactionRepository(recurringTransactionDao = recurringTransactionDao)

    @Provides
    @Singleton
    fun provideSavingPlanRepository(savingPlanDao: SavingPlanDao) =
        SavingPlanRepository(savingPlanDao = savingPlanDao)

    @Provides
    @Singleton
    fun provideWorkManager(application: Application) =
        WorkManager.getInstance(application)
}