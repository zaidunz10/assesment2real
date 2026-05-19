package com.zidansyahidagrifasa0072.assesment2.data.repository

import com.zidansyahidagrifasa0072.assesment2.data.dao.ProgressHistoryDao
import com.zidansyahidagrifasa0072.assesment2.data.dao.ProjectDao
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProgressHistoryEntity
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

class CctvRepository(
    private val projectDao: ProjectDao,
    private val progressHistoryDao: ProgressHistoryDao
) {
    val allActiveProjects: Flow<List<ProjectEntity>> = projectDao.getAllActiveProjects()
    val trashProjects: Flow<List<ProjectEntity>> = projectDao.getTrashProjects()

    suspend fun getProjectById(id: Long): ProjectEntity? = projectDao.getProjectById(id)

    suspend fun insertProject(project: ProjectEntity): Long = projectDao.insertProject(project)

    suspend fun updateProject(project: ProjectEntity) = projectDao.updateProject(project)

    suspend fun deleteProjectPermanently(id: Long) = projectDao.deleteProjectPermanently(id)

    suspend fun autoDeleteOldTrash(currentTime: Long, threshold: Long) =
        projectDao.autoDeleteOldTrash(currentTime, threshold)

    fun getHistoryForProject(projectId: Long): Flow<List<ProgressHistoryEntity>> =
        progressHistoryDao.getHistoryForProject(projectId)

    suspend fun insertHistory(history: ProgressHistoryEntity) =
        progressHistoryDao.insertHistory(history)
}