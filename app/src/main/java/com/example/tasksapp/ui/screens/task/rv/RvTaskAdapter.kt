package com.example.tasksapp.ui.screens.task.rv

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasksapp.R
import com.example.tasksapp.di.task


class RvTaskAdapter(private var tasks: MutableList<task>,
                    private val onTaskComplete: (task) -> Unit,
                    private val onTaskClick: (task) -> Unit,
                    private val onDeleteClick: (task) -> Unit
) : RecyclerView.Adapter<RvTaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxActivity)
        private val textView: TextView = itemView.findViewById(R.id.textViewActivityName)
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewDelete)

        fun bind(task: task) {
            textView.text = task.title
            checkBox.isChecked = task.isComplete
            textView.paintFlags = if (task.isComplete) {
                textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onTaskComplete(task)
                }
            }

            textView.setOnClickListener {
                onTaskClick(task)
            }

            imageView.setOnClickListener {
                onDeleteClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasks(newTasks: List<task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}