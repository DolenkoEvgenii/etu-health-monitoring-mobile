package ru.etu.monitoring.ui.fragment.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_create_task.*
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.presentation.presenter.task.CreateTaskPresenter
import ru.etu.monitoring.presentation.view.task.CreateTaskView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskFragment : BaseMvpFragment(), CreateTaskView {
    @InjectPresenter
    lateinit var presenter: CreateTaskPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        listOf(etTakeDate, etMedicineName, etPerDay).forEach {
            it.doAfterTextChanged {
                validateInput()
            }
        }
        btCreate.isEnabled = false
        btCreate.click {
            presenter.onCreateTaskClick(
                etMedicineName.text.toString(),
                etTakeDate.text.toString(),
                etPerDay.text.toString().toIntOrNull() ?: 1,
            )
        }

        btSelectDateTo.click {
            showPickDateDialog()
        }
    }

    private fun validateInput() {
        var isValid = true

        if (etMedicineName.text?.toString().orEmpty().isBlank()) {
            isValid = false
        }

        if (etTakeDate.text?.toString().orEmpty().isBlank()) {
            isValid = false
        }

        if (etPerDay.text?.toString().orEmpty().toIntOrNull() == null) {
            isValid = false
        }

        btCreate.isEnabled = isValid
    }

    private fun showPickDateDialog() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        val currentInput = etTakeDate.text?.toString().orEmpty()
        val currentDate = if (currentInput.isBlank()) Date() else sdf.parse(currentInput)
        val cal = Calendar.getInstance().apply { time = currentDate }

        val pickListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val dateStr = "%d-%02d-%02d".format(year, month + 1, dayOfMonth)
            etTakeDate.setText(dateStr)
        }

        DatePickerDialog(
            requireContext(),
            pickListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = provideSimpleToolbar(getString(R.string.create_task), appBar)
        appBar.addView(toolbar)
        return toolbar
    }

    companion object {
        const val TAG = "CreateTaskFragment"
        private const val DATE_FORMAT = "yyyy-MM-dd"


        fun newInstance(): CreateTaskFragment {
            return CreateTaskFragment()
        }
    }
}
