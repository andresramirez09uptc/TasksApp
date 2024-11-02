package com.example.tasksapp.ui.screens.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasksapp.R
import com.example.tasksapp.data.TaskViewModel
import com.example.tasksapp.di.task
import com.example.tasksapp.ui.screens.task.rv.RvTaskAdapter
import kotlinx.coroutines.launch

class TaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: RvTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        val editText: EditText = view.findViewById(R.id.editTextInput)
        val btnGuardar: Button = view.findViewById(R.id.btnGuardar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewActivities)
        val btnCompletar: Button = view.findViewById(R.id.btnCompletar)

        adapter = RvTaskAdapter(
            mutableListOf(),
            { task -> viewModel.completeTask(task.id) },
            { task -> showTaskDetails(task) },
            { task -> viewModel.deleteTask(task.id) }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        btnGuardar.setOnClickListener {
            val taskTitle = editText.text.toString()
            if (taskTitle.isNotEmpty()) {
                viewModel.addTask(taskTitle)
                editText.text.clear()
            }
        }

        btnCompletar.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_taskCompleteFragment)
        }

        initUiStateLifecycle()
        return view
    }

    private fun initUiStateLifecycle() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                val incompleteTasks = uiState.tasks.filter { !it.isComplete }
                adapter.updateTasks(incompleteTasks)
            }
        }
    }

    private fun showTaskDetails(task: task) {
        val action = TaskFragmentDirections.actionTaskFragmentToTaskDetailsFragment(
            task.id,
            task.title,
            task.isComplete
        )
        findNavController().navigate(action)
    }
}
