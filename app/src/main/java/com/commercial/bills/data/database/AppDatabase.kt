package com.commercial.bills.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.commercial.bills.data.dao.BalanceDao
import com.commercial.bills.data.dao.LastTransactionDao
import com.commercial.bills.data.dao.RecurringTransactionDao
import com.commercial.bills.data.dao.SavingPlanDao
import com.commercial.bills.data.entity.Balance
import com.commercial.bills.data.entity.LastTransaction
import com.commercial.bills.data.entity.RecurringTransaction
import com.commercial.bills.data.entity.SavingPlan
import com.commercial.bills.data.util.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities =
    [Balance::class,
    LastTransaction::class,
    RecurringTransaction::class,
    SavingPlan::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao() : BalanceDao
    abstract fun lastTransactionDao() : LastTransactionDao
    abstract fun recurringTransactionDao() : RecurringTransactionDao
    abstract fun savingPlanDao() : SavingPlanDao

    companion object {
        private const val DATABASE_NAME = "app_database"
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application) : AppDatabase {
            synchronized(this) {
                return INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(application).also { INSTANCE = it }
                }
            }
        }

        private fun buildDatabase(application: Application) : AppDatabase {
            return Room
                .databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                val balanceDao = getInstance(application).balanceDao()

                                balanceDao.insertBalance(Balance())
                            }
                        }
                    }
                )
                .addTypeConverter(Converters())
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}