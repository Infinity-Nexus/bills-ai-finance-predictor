package com.commercial.bills.data.repository

import com.commercial.bills.data.dao.LastTransactionDao
import com.commercial.bills.data.entity.LastTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LastTransactionRepository(
    private val lastTransactionDao: LastTransactionDao
) {
    fun getLastTransaction(id: Int) = lastTransactionDao.getLastTransaction(id)

    fun getLastTransactions() : Flow<List<LastTransaction>> {
        return lastTransactionDao
            .getLastTransactions()
            .map { lastTransaction ->
                lastTransaction.sortedByDescending { it.date.timeInMillis }
            }
    }

    suspend fun insertLastTransaction(lastTransaction: LastTransaction) {
        lastTransactionDao.insertLastTransaction(lastTransaction)
    }

    suspend fun deleteLastTransaction(lastTransaction: LastTransaction) {
        lastTransactionDao.deleteLastTransaction(lastTransaction)
    }
}