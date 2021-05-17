package ru.etu.monitoring.ui.adapter.item

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_task.*
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.utils.helpers.click
import ru.etu.monitoring.utils.helpers.gone
import ru.etu.monitoring.utils.helpers.visible

open class RequestTaskItem(
    private val task: RequestTask,
    private val canDelete: Boolean,
    private val canAccept: Boolean,
    private val listener: RequestTaskItemListener
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            tvTaskTitle.text = task.title
            tvTaskDate.text = task.plannedAt


            if (canDelete) {
                btDeleteTask.visible()
                btDeleteTask.click {
                    listener.onDeleteClick(task)
                }
            } else {
                btDeleteTask.gone()
            }

            if (canAccept) {
                btAcceptTask.visible()
                btAcceptTask.click {
                    listener.onAcceptClick(task)
                }
            } else {
                btAcceptTask.gone()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_task

    interface RequestTaskItemListener {
        fun onDeleteClick(task: RequestTask)
        fun onAcceptClick(task: RequestTask)
    }
}