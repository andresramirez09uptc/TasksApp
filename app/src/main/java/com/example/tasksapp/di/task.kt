package com.example.tasksapp.di

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isComplete: Boolean
)
