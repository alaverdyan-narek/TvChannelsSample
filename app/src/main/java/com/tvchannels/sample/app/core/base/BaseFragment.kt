package com.tvchannels.sample.app.core.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.tvchannels.sample.coreui.extension.collectWhenStarted
import com.tvchannels.sample.app.core.ext.navigateSafe
import com.tvchannels.sample.app.core.navigation.Command
import com.tvchannels.sample.app.core.util.manager.InfoEventCollector
import com.tvchannels.sample.app.core.util.manager.InfoEventCollectorImpl
import com.tvchannels.sample.app.core.util.manager.SystemPaddingParams
import com.tvchannels.sample.coreui.extension.addSystemPadding
import com.tvchannels.sample.coreui.extension.applyFullScreen
import com.tvchannels.sample.coreui.extension.hideKeyboard
import com.tvchannels.sample.coreui.extension.removeFullScreen
import com.tvchannels.sample.domain.extensions.onFalse

/**
 * All Fragments have to inherit from  [BaseFragment] class .
 */

abstract class BaseFragment<VM>(@LayoutRes layout: Int) :
    Fragment(layout),
    InfoEventCollector by InfoEventCollectorImpl()
        where VM : BaseViewModel {
    abstract val viewModel: VM

    open val applySystemPaddings: Boolean = true
    open val applyFullScreen: Boolean = false
    open val systemPaddingParams: SystemPaddingParams = SystemPaddingParams()

    private var navController: NavController? = null
    protected open val navControllerRes: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applySystemPaddings(view)
        applyFullScreen(view)
        navController = getNavController()
        collectWhenStarted(viewModel.command, ::processCommand)
        collectInfoEvents(host = this, viewModel.infoEvent)
        initView()
        initActions()
        initObservers()
    }

    private fun applyFullScreen(view: View) {
        if (!applyFullScreen) return
        view.applyFullScreen(requireActivity().window)
    }

    protected open fun initView() {}
    protected open fun initActions() {}
    protected open fun initObservers() {}

    private fun applySystemPaddings(view: View) {
        if (!applySystemPaddings) return
        view.addSystemPadding(
            isTop = systemPaddingParams.isTop,
            isBottom = systemPaddingParams.isBottom,
            isIncludeIme = systemPaddingParams.isIncludeIme,
        )
    }

    /**
     * This function triggers app navigation commands
     * @param Command  command of Navigation
     */

    protected open fun processCommand(command: Command) {
        when (command) {
            is Command.FinishAppCommand -> activity?.finishAffinity()

            is Command.NavigateUpCommand ->  (navController?.popBackStack() ?: false).onFalse {
                activity?.finishAffinity()
            }

            is Command.NavCommand -> if (command.isNested)
                getParentNavController()?.navigateSafe(
                    command.navDirections,
                    command.navOptions
                )
            else
                navController?.navigateSafe(command.navDirections, command.navOptions)

        }
    }

    private fun getNavController() = try {
        navControllerRes?.let { resId ->
            Navigation.findNavController(requireActivity(), resId)
        } ?: findNavController()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    fun turnOffFullScreen() {
        view?.removeFullScreen(requireActivity().window)
    }

    override fun onResume() {
        viewModel.onResume()
        super.onResume()
    }

    override fun onStart() {
        viewModel.onStart()
        super.onStart()
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()

    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }


    private fun getParentNavController() = parentFragment?.parentFragment
        ?.parentFragmentManager?.primaryNavigationFragment?.findNavController()


    override fun onDestroyView() {
        requireView().hideKeyboard()
        super.onDestroyView()
    }
}