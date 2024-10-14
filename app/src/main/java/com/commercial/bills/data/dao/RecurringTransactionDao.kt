package com.commercial.bills.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.commercial.bills.data.entity.RecurringTransaction
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

@Dao
interface RecurringTransactionDao {
    @Query("SELECT * FROM recurring_transaction_table WHERE id = :id LIMIT 1")
    fun getRecurringTransaction(id: Int) : Flow<RecurringTransaction>

    @Query("SELECT * FROM recurring_transaction_table")
    fun getRecurringTransactions() : Flow<List<RecurringTransaction>>

    @Query("UPDATE recurring_transaction_table SET next_time_to_invoke = :newDate WHERE id = :id")
    suspend fun setRecurringTransactionLastInvoked(newDate: Calendar, id: Int)

    @Query("SELECT * FROM recurring_transaction_table")
    suspend fun getRecurringTransactionsNoFlow() : List<RecurringTransaction>

    @Upsert
    suspend fun insertRecurringTransaction(recurringTransaction: RecurringTransaction)

    @Delete
    suspend fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction)
}