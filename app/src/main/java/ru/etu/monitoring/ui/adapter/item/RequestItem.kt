package ru.etu.monitoring.ui.adapter.item

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_request.*
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.utils.helpers.click

open class RequestItem(val request: Request, private val listener: RequestItemListener) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvPatientName.text = viewHolder.itemView.context.getString(R.string.patient_s_age, request.name, request.age)
        viewHolder.tvSymptoms.text = viewHolder.itemView.context.getString(R.string.symptoms_s, request.symptoms)
        viewHolder.tvTemperature.text = viewHolder.itemView.context.getString(R.string.temperature_s, request.temperature)

        viewHolder.itemView.click { listener.onRequestClick(request) }
    }

    override fun getLayout(): Int = R.layout.item_request

    interface RequestItemListener {
        fun onRequestClick(request: Request)
    }
}