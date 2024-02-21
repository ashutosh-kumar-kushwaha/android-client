/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.mifosxdroid.online.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mifos.mifosxdroid.activity.home.HomeActivity
import com.mifos.mifosxdroid.R
import com.mifos.mifosxdroid.adapters.SearchAdapter
import com.mifos.mifosxdroid.core.MifosBaseFragment
import com.mifos.mifosxdroid.core.util.Toaster.show
import com.mifos.mifosxdroid.databinding.FragmentClientSearchBinding
import com.mifos.objects.SearchedEntity
import com.mifos.objects.navigation.ClientArgs
import com.mifos.utils.Constants
import com.mifos.utils.Network
import dagger.hilt.android.AndroidEntryPoint
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig


@AndroidEntryPoint
class SearchFragment : MifosBaseFragment() {

    private val viewModel by viewModels<SearchViewModel>()
    private var resources: String? = null
    private var isFabOpen = false
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation
    private lateinit var layoutManager: LinearLayoutManager
    private var checkedFilter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as HomeActivity).supportActionBar?.title = getString(R.string.dashboard)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SearchScreen(
                    viewModel = viewModel
                ){ searchedEntity ->
                    onSearchOptionClick(searchedEntity)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.fabClient.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_dashboard_to_createNewClientFragment)
//        }
//
//        binding.fabCenter.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_dashboard_to_createNewCenterFragment)
//        }
//
//        binding.fabGroup.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_dashboard_to_createNewGroupFragment)
//        }
//
//        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                onClickSearch()
//                return@setOnEditorActionListener true
//            }
//            return@setOnEditorActionListener false
//        }
//
//
//        binding.btnSearch.setOnClickListener {
//            onClickSearch()
//        }
//
//        binding.fabCreate.setOnClickListener {
//            if (isFabOpen) {
//                binding.fabCreate.startAnimation(rotateBackward)
//                binding.fabClient.startAnimation(fabClose)
//                binding.fabCenter.startAnimation(fabClose)
//                binding.fabGroup.startAnimation(fabClose)
//                binding.fabClient.isClickable = false
//                binding.fabCenter.isClickable = false
//                binding.fabGroup.isClickable = false
//                isFabOpen = false
//            } else {
//                binding.fabCreate.startAnimation(rotateForward)
//                binding.fabClient.startAnimation(fabOpen)
//                binding.fabCenter.startAnimation(fabOpen)
//                binding.fabGroup.startAnimation(fabOpen)
//                binding.fabClient.isClickable = true
//                binding.fabCenter.isClickable = true
//                binding.fabGroup.isClickable = true
//                isFabOpen = true
//            }
//            autoTriggerSearch = false
//        }
    }

    private fun onSearchOptionClick(searchedEntity: SearchedEntity) {
        when (searchedEntity.entityType) {
            Constants.SEARCH_ENTITY_LOAN -> {
                val action = SearchFragmentDirections.actionNavigationDashboardToClientActivity(
                    ClientArgs(clientId = searchedEntity.entityId)
                )
                findNavController().navigate(action)
            }

            Constants.SEARCH_ENTITY_CLIENT -> {
                val action = SearchFragmentDirections.actionNavigationDashboardToClientActivity(
                    ClientArgs(clientId = searchedEntity.entityId)
                )
                findNavController().navigate(action)
            }

            Constants.SEARCH_ENTITY_GROUP -> {
                val action = searchedEntity.entityName?.let {
                    SearchFragmentDirections.actionNavigationDashboardToGroupsActivity(
                        searchedEntity.entityId,
                        it
                    )
                }
                action?.let { findNavController().navigate(it) }
            }

            Constants.SEARCH_ENTITY_SAVING -> {
                val action = SearchFragmentDirections.actionNavigationDashboardToClientActivity(
                    ClientArgs(savingsAccountNumber = searchedEntity.entityId)
                )
                findNavController().navigate(action)
            }

            Constants.SEARCH_ENTITY_CENTER -> {
                val action =
                    SearchFragmentDirections.actionNavigationDashboardToCentersActivity(
                        searchedEntity.entityId
                    )
                findNavController().navigate(action)
            }
        }
    }

//    private fun showGuide() {
//        val config = ShowcaseConfig()
//        config.delay = 250 // half second between each showcase view
//        val sequence = MaterialShowcaseSequence(activity, "123")
//        sequence.setConfig(config)
//        var etSearchIntro: String = getString(R.string.et_search_intro)
//        var i = 1
//        for (s: String in searchOptionsValues) {
//            etSearchIntro += "\n$i.$s"
//            i++
//        }
//        val spSearchIntro = getString(R.string.sp_search_intro)
//        val cbExactMatchIntro = getString(R.string.cb_exactMatch_intro)
//        val btSearchIntro = getString(R.string.bt_search_intro)
//        sequence.addSequenceItem(
//            binding.etSearch,
//            etSearchIntro, getString(R.string.got_it)
//        )
//        sequence.addSequenceItem(
//            binding.filterSelectionButton,
//            spSearchIntro, getString(R.string.next)
//        )
//        sequence.addSequenceItem(
//            binding.cbExactMatch,
//            cbExactMatchIntro, getString(R.string.next)
//        )
//        sequence.addSequenceItem(
//            binding.btnSearch,
//            btSearchIntro, getString(R.string.finish)
//        )
//        sequence.start()
//    }



    companion object {
        private val LOG_TAG = SearchFragment::class.java.simpleName
    }
}