package com.commercial.bills.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "balance_table")
data class Balance(
    @ColumnInfo(name = "balance")
    var balance: Float = 0f
) {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 1
}