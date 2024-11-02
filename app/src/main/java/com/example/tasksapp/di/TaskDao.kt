package com.example.tasksapp.di

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<task>
    @Insert
    suspend fun insertTask(task: task)
    @Delete
    suspend fun deleteTask(task: task)
    @Update
    suspend fun updateTask(task: task)
}