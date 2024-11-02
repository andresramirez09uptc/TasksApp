package com.example.tasksapp.ui.screens.taskcomplete

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasksapp.R
import com.example.tasksapp.data.TaskViewModel
import com.example.tasksapp.di.task
import com.example.tasksapp.ui.screens.task.TaskFragmentDirections
import com.example.tasksapp.ui.screens.taskcomplete.rv.RvTaskCompleteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskCompleteFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: RvTaskCompleteAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_complete, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        recyclerView = view.findViewById(R.id.recyclerViewCompletedTasks)
        recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                val completedTasks = uiState.tasks.filter { it.isComplete }
                if (completedTasks.isNotEmpty()) {
                    adapter = RvTaskCompleteAdapter(completedTasks,
                        { taskId -> viewModel.markAsIncomplete(taskId) },
                        { task -> showTaskDetails(task) },
                        { task -> viewModel.deleteTask(task.id) }
                    )
                    recyclerView.adapter = adapter
                } else {
                    recyclerView.adapter = null
                }
            }
        }
        return view
    }

    private fun showTaskDetails(task: task) {

        val action = TaskCompleteFragmentDirections.actionTaskCompleteFragmentToTaskDetailsFragment(
            task.id,
            task.title,
            task.isComplete
        )
        findNavController().navigate(action)
    }
}
