package com.zidansyahidagrifasa0072.assesment2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProgressHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressHistoryDao {
    @Query("SELECT * FROM progress_history WHERE projectId = :projectId ORDER BY id DESC")
    fun getHistoryForProject(projectId: Long): Flow<List<ProgressHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: ProgressHistoryEntity)
}