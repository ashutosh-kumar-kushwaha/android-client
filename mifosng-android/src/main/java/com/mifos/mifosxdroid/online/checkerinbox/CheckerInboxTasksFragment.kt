package com.mifos.mifosxdroid.online.checkerinbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mifos.core.objects.checkerinboxandtasks.CheckerTask
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.core.MifosBaseFragment
import com.mifos.mifosxdroid.core.util.Toaster
import com.mifos.mifosxdroid.databinding.CheckerInboxTasksFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckerInboxTasksFragment : MifosBaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: CheckerInboxTasksFragmentBinding

    override fun onRefresh() {
        viewModel.loadCheckerTasks()
        viewModel.loadRescheduleLoanTasks()
    }

    companion object {
        fun newInstance() = CheckerInboxTasksFragment()
    }

    @Inject
    lateinit var factory: CheckerInboxViewModelFactory
    private lateinit var viewModel: CheckerInboxTasksViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CheckerInboxTasksFragmentBinding.inflate(inflater, container, false)
        setToolbarTitle(resources.getString(R.string.checker_inbox_and_pending_tasks))
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showMifosProgressBar()
        viewModel = ViewModelProviders.of(
            this, factory
        ).get(CheckerInboxTasksViewModel::class.java)

        viewModel.getCheckerTasks().observe(viewLifecycleOwner,
            Observer<List<CheckerTask>> {
                binding.badgeCheckerInbox.text = it.size.toString()
            })

        viewModel.getRescheduleLoanTasks().observe(
            viewLifecycleOwner
        ) {
            binding.badgeRescheduleLoan.text = it.size.toString()
        }

        viewModel.status.observe(viewLifecycleOwner,
            Observer { status ->
                status?.let {
                    hideMifosProgressBar()
                    swipeRefreshLayout.isRefreshing = false
                    if (status) {
                        binding.llCheckerInboxTasks.visibility = View.VISIBLE
                    } else {
                        Toaster.show(binding.root, getString(R.string.network_issue))
                    }
                }
            })

        binding.rlCheckerInbox.setOnClickListener {
            findNavController().navigate(R.id.action_checkerInboxTasksFragment_to_checkerInboxFragment)
        }
    }
}