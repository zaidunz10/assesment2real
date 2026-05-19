package com.zidansyahidagrifasa0072.assesment2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerName: String,
    val phone: String,
    val address: String,
    val totalCamera: Int,
    val cctvType: String,
    val technician: String,
    val installDate: String,
    val progressStatus: String,
    val notes: String,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null
)