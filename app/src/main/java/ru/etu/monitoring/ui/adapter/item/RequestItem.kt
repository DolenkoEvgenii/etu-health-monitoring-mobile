package ru.etu.monitoring.ui.adapter.item

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_request.*
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.Request

open class RequestItem(val request: Request, val listener: RequestItemListener) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvRequest.text = request.firstName
    }

    override fun getLayout(): Int = R.layout.item_request

    interface RequestItemListener {
        fun onRequestClick(request: Request)
    }
}