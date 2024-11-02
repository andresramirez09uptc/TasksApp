package com.example.tasksapp.ui.screens.task.uiState

import com.example.tasksapp.di.task


data class TaskUiState(
    val tasks: List<task> = emptyList(),
    val isLoading: Boolean = false
)
