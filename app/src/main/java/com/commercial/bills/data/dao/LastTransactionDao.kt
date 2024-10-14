package com.commercial.bills.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.commercial.bills.data.entity.LastTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface LastTransactionDao {
    @Query("SELECT * FROM last_transaction_table WHERE id = :id LIMIT 1")
    fun getLastTransaction(id: Int) : Flow<LastTransaction>

    @Query("SELECT * FROM last_transaction_table")
    fun getLastTransactions() : Flow<List<LastTransaction>>

    @Upsert
    suspend fun insertLastTransaction(lastTransaction: LastTransaction)

    @Delete
    suspend fun deleteLastTransaction(lastTransaction: LastTransaction)
}