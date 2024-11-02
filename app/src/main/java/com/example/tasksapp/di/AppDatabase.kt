package com.example.tasksapp.di

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}