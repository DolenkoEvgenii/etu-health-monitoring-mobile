package ru.etu.monitoring.ui.fragment.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_request_details.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.presentation.presenter.doctor.DoctorRequestDetailsPresenter
import ru.etu.monitoring.presentation.view.doctor.DoctorRequestDetailsView
import ru.etu.monitoring.ui.adapter.item.RequestTaskItem
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click
import ru.etu.monitoring.utils.helpers.gone
import ru.etu.monitoring.utils.helpers.visible
import java.text.SimpleDateFormat
import java.util.*

class DoctorRequestDetailsFragment : BaseMvpFragment(), DoctorRequestDetailsView, RequestTaskItem.RequestTaskItemListener {
    @InjectPresenter
    lateinit var presenter: DoctorRequestDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): DoctorRequestDetailsPresenter {
        val request = arguments?.getSerializable(ARG_REQUEST) as Request
        return DoctorRequestDetailsPresenter(request)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTasks.layoutManager = LinearLayoutManager(context)

        btAcceptRequest.click {
            presenter.onAcceptRequestClick()
        }

        btAddTask.click {
            presenter.onAddTaskClick()
        }
    }

    override fun onDeleteClick(task: RequestTask) {
        presenter.onDeleteTaskClick(task)
    }

    override fun onAcceptClick(task: RequestTask) {

    }

    override fun showTasks(active: List<RequestTask>, done: List<RequestTask>, removed: List<RequestTask>) {
        vTasksView.visibility = View.VISIBLE
        val groupAdapter = GroupAdapter<GroupieViewHolder>()

        groupAdapter.addAll(active.map { RequestTaskItem(it, canDelete = true, canAccept = false, listener = this) })
        tvNoTask.visibility = if (active.isEmpty()) View.VISIBLE else View.GONE
        btAddTask.visible()

        rvTasks.adapter = groupAdapter

        chipActive.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                groupAdapter.clear()
                groupAdapter.addAll(active.map { RequestTaskItem(it, canDelete = true, canAccept = false, listener = this) })
                tvNoTask.visibility = if (active.isEmpty()) View.VISIBLE else View.GONE
                btAddTask.visible()
            }
        }

        chipDone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                groupAdapter.clear()
                groupAdapter.addAll(done.map { RequestTaskItem(it, canDelete = false, canAccept = false, listener = this) })
                tvNoTask.visibility = if (done.isEmpty()) View.VISIBLE else View.GONE
                btAddTask.gone()
            }
        }

        chipDeleted.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                groupAdapter.clear()
                groupAdapter.addAll(removed.map { RequestTaskItem(it, canDelete = false, canAccept = false, listener = this) })
                tvNoTask.visibility = if (removed.isEmpty()) View.VISIBLE else View.GONE
                btAddTask.gone()
            }
        }
    }

    override fun bindRequest(request: Request) {
        tvPatientName.text = getString(R.string.patient_s, request.name)
        tvPatientAge.text = getString(R.string.age_s, request.age)
        tvCreateDate.text = getString(
            R.string.create_date_s,
            SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(request.createdAt)
        )
        tvSymptoms.text = getString(R.string.symptoms_s, request.symptoms)
        tvTemperature.text = getString(R.string.temperature_s, request.temperature)

        tvStatus.text = when {
            request.isNew -> {
                getString(R.string.status_new)
            }
            request.isOpen -> {
                getString(R.string.status_current)
            }
            request.isClosed -> {
                getString(R.string.status_discharged)
            }
            else -> {
                "unknown status"
            }
        }

        btAcceptRequest.visibility = if (request.isNew) View.VISIBLE else View.GONE
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = provideSimpleToolbar(getString(R.string.request), appBar)
        appBar.addView(toolbar)
        return toolbar
    }

    companion object {
        const val TAG = "DoctorRequestDetailsFragment"
        private const val ARG_REQUEST = "request_arg"

        fun newInstance(request: Request): DoctorRequestDetailsFragment {
            return DoctorRequestDetailsFragment().apply {
                arguments = bundleOf(ARG_REQUEST to request)
            }
        }
    }
}
