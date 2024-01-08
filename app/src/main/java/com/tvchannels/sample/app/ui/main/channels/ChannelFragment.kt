package com.tvchannels.sample.app.ui.main.channels

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.tvchannels.sample.R
import com.tvchannels.sample.app.core.base.BaseFragment
import com.tvchannels.sample.app.ui.main.adapter.ChannelsAdapter
import com.tvchannels.sample.coreui.delegate.viewBinding
import com.tvchannels.sample.coreui.extension.collectWhenStarted
import com.tvchannels.sample.coreui.extension.errorDialog
import com.tvchannels.sample.coreui.utils.AdaptiveSpacingItemDecoration
import com.tvchannels.sample.databinding.FragmentChannelBinding
import com.tvchannels.sample.domain.extensions.dpToPx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelFragment : BaseFragment<ChannelViewModel>(R.layout.fragment_channel) {
    override val viewModel: ChannelViewModel by viewModels()
    private val binding by viewBinding(FragmentChannelBinding::bind)
    private val adapter by lazy {
        ChannelsAdapter(
            onItemClick = {
                viewModel.navigateToPlayer(it)
            },
            onFavClick = {
                viewModel.favAction(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvChannels.adapter = adapter.adapter
            rvChannels.addItemDecoration(AdaptiveSpacingItemDecoration(8.dpToPx, true))
        }
    }

    override fun initObservers() {
        collectWhenStarted(viewModel.state) {
            if (it is ChannelState.DataList) {
                val isEmpty = it.channels.isEmpty()
                binding.apply {
                    rvChannels.isVisible = isEmpty.not()
                    tvNoResultsFound.isVisible = isEmpty
                }
                adapter.adapter.submitList(it.channels)
            }
        }
        collectWhenStarted(viewModel.error) {
            errorDialog {
                viewModel.retry()
            }.show()
        }
    }

    companion object {
        fun newInstance() = ChannelFragment()
    }
}