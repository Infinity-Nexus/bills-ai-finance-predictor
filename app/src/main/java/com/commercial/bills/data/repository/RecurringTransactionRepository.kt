package com.commercial.bills.data.repository

import com.commercial.bills.data.dao.RecurringTransactionDao
import com.commercial.bills.data.entity.RecurringTransaction
import java.util.Calendar

class RecurringTransactionRepository(
    private val recurringTransactionDao: RecurringTransactionDao
) {
    fun getRecurringTransaction(id: Int) = recurringTransactionDao.getRecurringTransaction(id)

    suspend fun setRecurringTransactionNextTimeToInvoke(newDate: Calendar, id: Int) {
        recurringTransactionDao.setRecurringTransactionLastInvoked(newDate, id)
    }

    fun getRecurringTransactions() =
        recurringTransactionDao.getRecurringTransactions()

    suspend fun getRecurringTransactionsNoFlow() =
        recurringTransactionDao.getRecurringTransactionsNoFlow()


    suspend fun insertRecurringTransaction(recurringTransaction: RecurringTransaction) {
        recurringTransactionDao.insertRecurringTransaction(recurringTransaction)
    }

    suspend fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction) {
        recurringTransactionDao.deleteRecurringTransaction(recurringTransaction)
    }
}