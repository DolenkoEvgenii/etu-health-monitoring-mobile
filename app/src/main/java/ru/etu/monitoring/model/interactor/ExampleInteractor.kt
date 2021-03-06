package ru.etu.monitoring.model.interactor


import java.util.*

class ExampleInteractor {
    private val listeners: ArrayList<OnChangedListener> = ArrayList()

    fun subscribe(onChangedListener: OnChangedListener) {
        listeners.add(onChangedListener)
    }

    fun unSubscribe(onChangedListener: OnChangedListener) {
        listeners.remove(onChangedListener)
    }

    fun fireEvent(value: Int) {
        for (onChangedListener in listeners)
            onChangedListener.onChange(value)
    }

    interface OnChangedListener {
        fun onChange(value: Int)
    }
}
