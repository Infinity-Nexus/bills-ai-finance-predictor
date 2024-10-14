package com.commercial.bills.data.repository

import com.commercial.bills.data.dao.SavingPlanDao
import com.commercial.bills.data.entity.SavingPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class SavingPlanRepository(
    private val savingPlanDao: SavingPlanDao
) {
    fun getSavingPlan(id: Int) = savingPlanDao.getSavingPlan(id)

    fun getSavingPlans() : Flow<List<SavingPlan>> {
        return savingPlanDao
            .getSavingPlans()
            .map { savingPlan ->
                savingPlan.sortedBy { it.dateStart.timeInMillis }
            }
    }

    suspend fun getSavingPlansNoFlow() = savingPlanDao.getSavingPlansNoFlow()

    suspend fun setSavingPlanNextTimeToInvoke(newDate: Calendar, id: Int) {
        savingPlanDao.setSavingPlanLastInvoked(newDate, id)
    }

    suspend fun increaseSavedAmount(amount: Float, id: Int) {
        savingPlanDao.increaseSavedAmount(amount, id)
    }


    suspend fun insertSavingPlan(savingPlan: SavingPlan) {
        savingPlanDao.insertSavingPlan(savingPlan)
    }

    suspend fun deleteSavingPlan(savingPlan: SavingPlan) {
        savingPlanDao.deleteSavingPlan(savingPlan)
    }
}