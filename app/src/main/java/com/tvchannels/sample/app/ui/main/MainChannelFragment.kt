package com.tvchannels.sample.app.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.tvchannels.sample.R
import com.tvchannels.sample.app.core.adapter.TvChannelsViewPagerAdapter
import com.tvchannels.sample.app.core.base.BaseFragment
import com.tvchannels.sample.app.ui.main.channels.ChannelFragment
import com.tvchannels.sample.app.ui.main.favorites.FavoritesFragment
import com.tvchannels.sample.coreui.delegate.viewBinding
import com.tvchannels.sample.coreui.extension.collectWhenStarted
import com.tvchannels.sample.databinding.FragmentMainChannelBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainChannelFragment : BaseFragment<MainChannelViewModel>(R.layout.fragment_main_channel) {
    override val viewModel by viewModels<MainChannelViewModel>()
    private val binding by viewBinding(FragmentMainChannelBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tlSearch.editText?.doAfterTextChanged(viewModel.searchChange)
        }
    }

    override fun initObservers() {
        viewModel.mainState.onEach {
            binding.apply {
                vpChannels.adapter = TvChannelsViewPagerAdapter(this@MainChannelFragment).apply {
                    addFragment(ChannelFragment.newInstance())
                    addFragment(FavoritesFragment.newInstance())
                }
                vpChannels.offscreenPageLimit = it.offsetScreenPageLimit
                TabLayoutMediator(tabChannels, vpChannels) { tab, position ->
                    tab.text = it.tabNames[position].asString(requireContext())
                }.attach()
            }
        }.launchIn(lifecycleScope)
        collectWhenStarted(viewModel.showLoading){
            binding.group.isVisible = it.not()
            binding.loadingBar.isVisible = it
        }
    }
}