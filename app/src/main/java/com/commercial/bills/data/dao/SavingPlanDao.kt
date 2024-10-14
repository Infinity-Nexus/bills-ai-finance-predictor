package com.commercial.bills.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.commercial.bills.data.entity.SavingPlan
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

@Dao
interface SavingPlanDao {
    @Query("SELECT * FROM saving_plan_table WHERE id = :id LIMIT 1")
    fun getSavingPlan(id: Int) : Flow<SavingPlan>

    @Query("SELECT * FROM saving_plan_table ORDER BY date_start DESC")
    fun getSavingPlans() : Flow<List<SavingPlan>>

    @Query("SELECT * FROM saving_plan_table")
    suspend fun getSavingPlansNoFlow() : List<SavingPlan>

    @Query("UPDATE saving_plan_table SET saved_amount = saved_amount + :amount WHERE id = :id")
    suspend fun increaseSavedAmount(amount: Float, id: Int)

    @Query("UPDATE saving_plan_table SET next_time_to_invoke = :newDate WHERE id = :id")
    suspend fun setSavingPlanLastInvoked(newDate: Calendar, id: Int)

    @Upsert
    suspend fun insertSavingPlan(savingPlan: SavingPlan)

    @Delete
    suspend fun deleteSavingPlan(savingPlan: SavingPlan)
}