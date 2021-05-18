package ru.etu.monitoring.ui.fragment.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_patient_main.*
import kotlinx.android.synthetic.main.toolbar_logout.view.*
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.presentation.presenter.main.MainPatientPresenter
import ru.etu.monitoring.presentation.view.main.MainPatientView
import ru.etu.monitoring.service.LocationTrackingService
import ru.etu.monitoring.ui.adapter.item.RequestTaskItem
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click
import ru.etu.monitoring.utils.helpers.gone
import ru.etu.monitoring.utils.helpers.visible

class MainPatientFragment : BaseMvpFragment(), MainPatientView, RequestTaskItem.RequestTaskItemListener {
    @InjectPresenter
    lateinit var presenter: MainPatientPresenter

    private var serviceRef: LocationTrackingService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocationTrackingService.MyLocalBinder
            serviceRef = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceRef = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_patient_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTasks.layoutManager = LinearLayoutManager(context)
        btImIll.click { presenter.onImIllClick() }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun bindProfile(profile: User) {
        tvName.text = profile.firstName
        tvLastName.text = profile.lastName
        tvMiddleName.text = profile.middleName
        tvBirthDay.text = profile.birthdayStr
        tvIllInfo.text = if (profile.isIll) getString(R.string.you_ill) else getString(R.string.you_not_ill)

        if (profile.isIll) {
            btImIll.gone()

            if (profile.doctor == null) {
                vTreatments.gone()
                vDoctorCard.gone()
                tvNoDoctorInfo.visible()
            } else {
                tvNoDoctorInfo.gone()
                vDoctorCard.visible()
                vTreatments.visible()

                tvDoctorInfo.text = profile.doctor.fullName
                tvDoctorPhone.text = profile.doctor.phone

                btCall.click {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + profile.doctor.phone)))
                }
            }
        } else {
            btImIll.visible()
            tvNoDoctorInfo.gone()
            vDoctorCard.gone()
        }
    }

    override fun onDeleteClick(task: RequestTask) {

    }

    override fun onAcceptClick(task: RequestTask) {
        presenter.onMarkTaskDoneClick(task)
    }

    override fun showTasks(active: List<RequestTask>, done: List<RequestTask>) {
        vTasksView.visibility = View.VISIBLE
        val groupAdapter = GroupAdapter<GroupieViewHolder>()

        groupAdapter.addAll(active.map { RequestTaskItem(it, canDelete = false, canAccept = true, listener = this) })
        tvNoTask.visibility = if (active.isEmpty()) View.VISIBLE else View.GONE
        rvTasks.adapter = groupAdapter

        chipActive.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                groupAdapter.clear()
                groupAdapter.addAll(active.map { RequestTaskItem(it, canDelete = false, canAccept = true, listener = this) })
                tvNoTask.visibility = if (active.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        chipDone.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                groupAdapter.clear()
                groupAdapter.addAll(done.map { RequestTaskItem(it, canDelete = false, canAccept = false, listener = this) })
                tvNoTask.visibility = if (done.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun closeLoadingDialog() {
        vLoading.gone()
    }

    override fun showLoadingDialog() {
        vLoading.visible()
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = inflateToolbar(R.layout.toolbar_logout, appBar)
        toolbar.tvTitle.text = getString(R.string.info)
        toolbar.btLogout.click {
            presenter.onLogoutClick()
        }
        appBar.addView(toolbar)
        return toolbar
    }

    // Location Service Part
    override fun bindService() {
        val serviceIntent = Intent(context, LocationTrackingService::class.java)
        context?.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun startTrackingService() {
        val serviceIntent = Intent(context, LocationTrackingService::class.java)
        context?.startService(serviceIntent)
        bindService()
    }

    override fun stopTrackingService() {
        unbindService()
        context?.stopService(Intent(context, LocationTrackingService::class.java))
        serviceRef = null
    }

    private fun unbindService() {
        try {
            context?.unbindService(serviceConnection)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    override fun onDestroy() {
        unbindService()
        super.onDestroy()
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance(): MainPatientFragment {
            return MainPatientFragment()
        }
    }
}
