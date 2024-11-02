package com.example.tasksapp.ui.screens.taskdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasksapp.R
import com.example.tasksapp.data.TaskViewModel
import com.example.tasksapp.di.task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var task: task
    private val args: TaskDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_details, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        var task = task(args.id, args.title, args.complete)
        val textViewTaskName: TextView = view.findViewById(R.id.textViewTaskName)
        val textViewTaskStatus: TextView = view.findViewById(R.id.textViewTaskStatus)
        val checkBoxComplete: CheckBox = view.findViewById(R.id.checkBoxComplete)
        val imageView: ImageView = view.findViewById(R.id.imageViewDelete)


        textViewTaskName.text = task.title
        textViewTaskStatus.text = if (task.isComplete) "Completada" else "No completada"
        checkBoxComplete.isChecked = task.isComplete

        checkBoxComplete.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.completeTask(task.id)
                textViewTaskStatus.text = "Completada"
            } else {
                viewModel.markAsIncomplete(task.id)
                textViewTaskStatus.text = "No completada"
            }
        }
        imageView.setOnClickListener {
            viewModel.deleteTask(task.id)
            findNavController().popBackStack()
        }

        return view
    }

}
