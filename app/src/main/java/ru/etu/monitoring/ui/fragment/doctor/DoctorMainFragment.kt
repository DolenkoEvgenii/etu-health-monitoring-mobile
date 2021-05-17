package ru.etu.monitoring.ui.fragment.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_doctor_main.*
import kotlinx.android.synthetic.main.toolbar_logout.view.*
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.presentation.presenter.doctor.DoctorMainPresenter
import ru.etu.monitoring.presentation.presenter.doctor.DoctorMainPresenter.RequestType.*
import ru.etu.monitoring.presentation.view.doctor.DoctorMainView
import ru.etu.monitoring.ui.adapter.item.RequestItem
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click
import ru.etu.monitoring.utils.helpers.gone
import ru.etu.monitoring.utils.helpers.visible
import ru.etu.monitoring.utils.tool.pagination.paging

class DoctorMainFragment : BaseMvpFragment(), DoctorMainView, RequestItem.RequestItemListener {
    @InjectPresenter
    lateinit var presenter: DoctorMainPresenter

    private var currentType: DoctorMainPresenter.RequestType = NEW

    private var tabLayout: TabLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doctor_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvNewRequests.layoutManager = LinearLayoutManager(context)
        rvCurrentRequests.layoutManager = LinearLayoutManager(context)
        rvFinishedRequests.layoutManager = LinearLayoutManager(context)

        onTabChanged(0)
        refreshLayout.setOnRefreshListener {
            presenter.onRefresh(currentType)
        }
    }

    override fun showNewRequests(requests: List<Request>) {
        showRequests(requests, NEW)
    }

    override fun showCurrentRequests(requests: List<Request>) {
        showRequests(requests, ACTIVE)
    }

    override fun showClosedRequests(requests: List<Request>) {
        showRequests(requests, FINISHED)
    }

    private fun showRequests(requests: List<Request>, type: DoctorMainPresenter.RequestType) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        groupAdapter.addAll(requests.map { RequestItem(it, this) })

        getRecyclerForType(type).apply {
            adapter = groupAdapter
            paging({ presenter.onLoadMore(type) }, emptyListCount = requests.size, limit = DoctorMainPresenter.PAGE_SIZE)
        }
    }

    override fun addRequests(requests: List<Request>, type: DoctorMainPresenter.RequestType) {
        (getRecyclerForType(type).adapter as? GroupAdapter)?.addAll(requests.map { RequestItem(it, this) })
    }

    override fun clearRequests(type: DoctorMainPresenter.RequestType) {
        (getRecyclerForType(type).adapter as? GroupAdapter)?.clear()
    }

    override fun onRequestClick(request: Request) {
        presenter.onRequestClick(request)
    }

    private fun onTabChanged(pos: Int) {
        when (pos) {
            TAB_NEW -> {
                tabLayout?.selectTab(tabLayout?.getTabAt(0))
                currentType = NEW
                rvNewRequests.visible()
                rvCurrentRequests.gone()
                rvFinishedRequests.gone()
            }
            TAB_CURRENT -> {
                tabLayout?.selectTab(tabLayout?.getTabAt(1))
                currentType = ACTIVE
                rvNewRequests.gone()
                rvCurrentRequests.visible()
                rvFinishedRequests.gone()
            }
            TAB_FINISHED -> {
                tabLayout?.selectTab(tabLayout?.getTabAt(2))
                currentType = FINISHED
                rvNewRequests.gone()
                rvCurrentRequests.gone()
                rvFinishedRequests.visible()
            }
        }
    }

    private fun getRecyclerForType(type: DoctorMainPresenter.RequestType): RecyclerView {
        return when (type) {
            NEW -> rvNewRequests
            ACTIVE -> rvCurrentRequests
            FINISHED -> rvFinishedRequests
        }
    }

    override fun showLoadingDialog() {
        refreshLayout.isRefreshing = true
    }

    override fun closeLoadingDialog() {
        refreshLayout.isRefreshing = false
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = inflateToolbar(R.layout.toolbar_logout, appBar)
        toolbar.tvTitle.text = getString(R.string.requests_list)
        toolbar.btLogout.click {
            presenter.onLogoutClick()
        }
        appBar.addView(toolbar)

        if (tabLayout == null) {
            val tabLayout = TabLayout(requireContext()).apply {
                addTab(newTab().setText(context.getString(R.string.new_)))
                addTab(newTab().setText(context.getString(R.string.current)))
                addTab(newTab().setText(context.getString(R.string.closed)))
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    onTabChanged(tab?.position ?: 0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
            this.tabLayout = tabLayout
            appBar.addView(tabLayout)
        } else {
            appBar.addView(tabLayout)
        }

        return toolbar
    }

    companion object {
        private const val TAB_NEW = 0
        private const val TAB_CURRENT = 1
        private const val TAB_FINISHED = 2

        const val TAG = "DoctorMainFragment"
        fun newInstance(): DoctorMainFragment {
            return DoctorMainFragment()
        }
    }
}
