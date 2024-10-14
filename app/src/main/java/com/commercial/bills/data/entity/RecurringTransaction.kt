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
@Entity(tableName = "recurring_transaction_table")
data class RecurringTransaction(
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "amount")
    val amount: Float = 0f,
    @ColumnInfo(name = "is_debit")
    val isDebit: Boolean = true,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "recurring_type")
    val recurringType: RecurringType = RecurringType.MONTHLY
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
                it.set(Calendar.DAY_OF_MONTH, 1)
            }
            RecurringType.YEARLY -> {
                it.add(Calendar.YEAR, 1)
                it.set(Calendar.DAY_OF_YEAR, 1)
            }
            else -> {}
        }
    }

    fun invoke() : LastTransaction {
        return LastTransaction(
            name = name,
            amount = amount,
            isDebit = isDebit,
            recurringType = recurringType
        )
    }
}
