package com.example.tasksapp.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.di.TaskDao
import com.example.tasksapp.di.task
import com.example.tasksapp.ui.screens.task.uiState.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()
    private val taskList = mutableListOf<task>()

    init {
        loadTasks() // Cargar tareas al inicializar el ViewModel
    }

    private fun loadTasks() {
        viewModelScope.launch {
            // Obtener las tareas de la base de datos y actualizar la lista
            taskList.clear() // Limpiar la lista existente
            taskList.addAll(taskDao.getAllTasks()) // Asumiendo que devuelve List<task>
            _uiState.value = TaskUiState(tasks = taskList.toList())
        }
    }

    fun addTask(taskTitle: String) {
        val newTask = task(
            title = taskTitle,
            isComplete = false
        )
        taskList.add(newTask)
        _uiState.value = TaskUiState(tasks = taskList.toList())
        save(newTask)
    }

    fun completeTask(taskId: Int) {
        taskList.replaceAll { task ->
            if (task.id == taskId) task.copy(isComplete = true) else task
        }
        _uiState.value = TaskUiState(tasks = taskList.toList())

        Log.d("ViewModel", "Tasks after completion: ${_uiState.value.tasks}")
        val completedTask = taskList.find { task -> task.id == taskId }
        completedTask?.let { update(it) }
    }

    private fun generateId(): Int {
        return taskList.size + 1
    }

    fun markAsIncomplete(taskId: Int) {
        taskList.replaceAll { task ->
            if (task.id == taskId) task.copy(isComplete = false) else task
        }
        _uiState.value = TaskUiState(tasks = taskList.toList())
        val incompletedTask = taskList.find { task -> task.id == taskId }
        incompletedTask?.let { update(it) }
    }

    private fun deleteFromDb(task: task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
    fun deleteTask(taskId: Int) {
        val taskToRemove = taskList.find { task -> task.id == taskId }
        taskToRemove?.let {
            taskList.remove(it)
            _uiState.value = TaskUiState(tasks = taskList.toList())
            deleteFromDb(it)
        }
    }

    private fun save(task: task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    private fun update(task: task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }
}
