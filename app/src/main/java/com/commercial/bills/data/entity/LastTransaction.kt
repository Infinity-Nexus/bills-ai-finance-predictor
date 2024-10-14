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
@Entity(tableName = "last_transaction_table")
data class LastTransaction(
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "amount")
    val amount: Float = 0f,
    @ColumnInfo(name = "is_debit")
    val isDebit: Boolean = true,
    @ColumnInfo(name = "recurring_type")
    val recurringType: RecurringType = RecurringType.ONE_TIME,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "date")
    val date: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
