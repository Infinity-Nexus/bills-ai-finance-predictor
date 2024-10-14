package com.commercial.bills.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.commercial.bills.data.util.Converters
import com.commercial.bills.data.util.RecurringType
import java.util.Calendar
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "saving_plan_table")
data class SavingPlan(
    @ColumnInfo(name = "name")
    val name: String= "",
    @ColumnInfo(name = "amount")
    val amount: Float = 0f,
    @ColumnInfo(name = "saved_amount")
    var savedAmount: Float = 0f,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "date_start")
    val dateStart: Calendar = Calendar.getInstance(),
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "date_end")
    val dateEnd: Calendar = Calendar.getInstance(),
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "recurring_type")
    val recurringType: RecurringType = RecurringType.DAILY
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "next_time_to_invoke")
    var nextTimeToInvoke: Calendar = Calendar.getInstance().also {
        when(recurringType) {
            RecurringType.DAILY -> {
                it.add(Calendar.DAY_OF_YEAR, 1)
            }
            RecurringType.WEEKLY -> {
                it.add(Calendar.WEEK_OF_YEAR, 1)
            }
            RecurringType.MONTHLY -> {
                it.add(Calendar.MONTH, 1)
            }
            RecurringType.YEARLY -> {
                it.add(Calendar.YEAR, 1)
            }
            else -> {}
        }
    }

    fun amountToSave() : Float {
        val planYears = dateEnd.get(Calendar.YEAR) - dateStart.get(Calendar.YEAR)
        var planDays = dateEnd.get(Calendar.DAY_OF_YEAR) - dateStart.get(Calendar.DAY_OF_YEAR)
        var planWeeks = dateEnd.get(Calendar.WEEK_OF_YEAR) - dateStart.get(Calendar.WEEK_OF_YEAR)
        var planMonths = dateEnd.get(Calendar.MONTH) - dateStart.get(Calendar.MONTH)

        if(planYears > 0) {
            val daysOfYear = dateStart.getMaximum(Calendar.DAY_OF_YEAR)

            planDays = daysOfYear -
                    dateStart.get(Calendar.DAY_OF_YEAR) +
                    dateEnd.get(Calendar.DAY_OF_YEAR)

            planWeeks = planDays / 7

            planMonths = planWeeks / 4
        }



        val amountToSave = when(recurringType) {
            RecurringType.DAILY -> {
                amount / planDays
            }
            RecurringType.WEEKLY -> {
                amount / planWeeks
            }
            RecurringType.MONTHLY -> {
                amount / planMonths
            }
            RecurringType.YEARLY -> {
                amount / planYears
            }
            else -> {
                amount
            }
        }

        return if(amountToSave.isInfinite()) {
            amount
        }
        else {
            amountToSave
        }
    }

    fun invoke() : LastTransaction {
        return LastTransaction(
            name = name,
            amount = amount,
            recurringType = recurringType
        )
    }
}
