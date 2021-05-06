package com.mvp.mvptemplate.utils.other

class CompositeBoolean {
    val booleans: MutableList<Boolean?> = ArrayList()

    val isAllTrue: Boolean
        get() = booleans.all { it == true }

    fun add(boolean: Boolean?) {
        booleans.add(boolean)
    }
}