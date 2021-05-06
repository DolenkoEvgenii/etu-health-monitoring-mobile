package com.mvp.mvptemplate.utils.helpers

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.inputLayout(): TextInputLayout {
    var vParent = parent
    while (vParent != null) {
        if (vParent is TextInputLayout) {
            return vParent
        } else {
            vParent = vParent.parent
        }
    }
    throw IllegalStateException("no input layout parent")
}

fun TextInputEditText.showError(resId: Int) {
    inputLayout().showError(resId)
}

fun TextInputEditText.hideError() {
    inputLayout().hideError()
}

fun TextInputEditText.setErrorIf(resId: Int, condition: () -> Boolean) {
    inputLayout().setErrorIf(resId, condition)
}

fun TextInputLayout.setErrorIf(resId: Int, condition: () -> Boolean) {
    if (condition()) {
        showError(resId)
    } else {
        hideError()
    }
}

fun TextInputLayout.showError(resId: Int) {
    error = context.getString(resId)
    isErrorEnabled = true
}

fun TextInputLayout.hideError() {
    isErrorEnabled = false
}
