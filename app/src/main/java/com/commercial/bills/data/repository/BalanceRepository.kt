package com.commercial.bills.data.repository

import com.commercial.bills.data.dao.BalanceDao
import com.commercial.bills.data.entity.Balance

class BalanceRepository(
    private val balanceDao: BalanceDao
) {
    suspend fun getBalance() = balanceDao.getBalance()

    fun getBalanceFlow() = balanceDao.getBalanceFlow()

    suspend fun increaseBalance(amount: Float) {
        balanceDao.increaseBalance(amount)
    }

    suspend fun decreaseBalance(amount: Float) {
        balanceDao.decreaseBalance(amount)
    }

    suspend fun insertBalance() = balanceDao.insertBalance(Balance())
}
