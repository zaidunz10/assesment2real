package com.zidansyahidagrifasa0072.assesment2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zidansyahidagrifasa0072.assesment2.data.dao.ProgressHistoryDao
import com.zidansyahidagrifasa0072.assesment2.data.dao.ProjectDao
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProgressHistoryEntity
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProjectEntity

@Database(entities = [ProjectEntity::class, ProgressHistoryEntity::class], version = 1, exportSchema = false)
abstract class CctvDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun progressHistoryDao(): ProgressHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CctvDatabase? = null

        fun getDatabase(context: Context): CctvDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CctvDatabase::class.java,
                    "cctv_manager_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}