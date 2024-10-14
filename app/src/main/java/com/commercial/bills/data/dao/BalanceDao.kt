package com.commercial.bills.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.commercial.bills.data.entity.Balance
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    @Query("SELECT * FROM balance_table WHERE id = 1 LIMIT 1")
    suspend fun getBalance() : Balance

    @Query("SELECT * FROM balance_table WHERE id = 1 LIMIT 1")
    fun getBalanceFlow() : Flow<Balance?>

    @Query("UPDATE balance_table SET balance = balance + :amount WHERE id = 1")
    suspend fun increaseBalance(amount: Float)

    @Query("UPDATE balance_table SET balance = balance - :amount WHERE id = 1")
    suspend fun decreaseBalance(amount: Float)

    @Upsert
    suspend fun insertBalance(balance: Balance)
}