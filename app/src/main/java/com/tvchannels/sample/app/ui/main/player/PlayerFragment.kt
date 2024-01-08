package com.tvchannels.sample.app.ui.main.player

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil.load
import com.tvchannels.sample.R
import com.tvchannels.sample.app.core.base.BaseFragment
import com.tvchannels.sample.app.utils.RDrawable
import com.tvchannels.sample.coreui.delegate.viewBinding
import com.tvchannels.sample.coreui.extension.collectWhenStarted
import com.tvchannels.sample.coreui.utils.setOnSingleClickListener
import com.tvchannels.sample.databinding.FragmentPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : BaseFragment<PlayerViewModel>(R.layout.fragment_player) {
    override val viewModel: PlayerViewModel by viewModels()
    private val binding by viewBinding(FragmentPlayerBinding::bind)

    private var player: Player? = null

    override fun initView() {
        binding.ivBack.setOnSingleClickListener {
            viewModel.navigateUp()
        }
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun initObservers() {
        collectWhenStarted(viewModel.state) {
            binding.apply {
                tvTitle.text = it.title
                tvSubTitle.text = it.subTitle
                ivChannel.load(it.imageUrl) {
                    error(RDrawable.ic_photo_error)
                }
            }
        }


    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()
    }


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onDestroyView() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onDestroyView()
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.playerView.player = exoPlayer
                val mediaItem = MediaItem.Builder()
                    //it gives 401 error  @todo when it will work we can replace constant link
//                    .setUri(Uri.parse(viewModel.args.streamUrl))
                    .setUri(Uri.parse("https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"))
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = true
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.playerView
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}