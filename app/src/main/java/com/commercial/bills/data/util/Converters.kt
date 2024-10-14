package com.commercial.bills.data.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.Calendar

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun calendarToTimeStamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun timeStampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun recurringTypeToString(recurringType: RecurringType): String {
        return when(recurringType) {
            RecurringType.ONE_TIME -> "one_time"
            RecurringType.DAILY -> "daily"
            RecurringType.WEEKLY -> "weekly"
            RecurringType.MONTHLY -> "monthly"
            RecurringType.YEARLY -> "yearly"
        }
    }

    @TypeConverter
    fun stringToRecurringType(value: String): RecurringType? {
        return when(value) {
            "one_time" -> RecurringType.ONE_TIME
            "daily" -> RecurringType.DAILY
            "weekly" -> RecurringType.WEEKLY
            "monthly" -> RecurringType.MONTHLY
            "yearly" -> RecurringType.YEARLY
            else -> null
        }
    }
}