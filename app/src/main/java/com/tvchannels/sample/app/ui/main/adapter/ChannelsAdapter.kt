package com.tvchannels.sample.app.ui.main.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import coil.load
import com.tvchannels.sample.R
import com.tvchannels.sample.app.core.util.manager.cachedMode
import com.tvchannels.sample.app.utils.RColor
import com.tvchannels.sample.app.utils.RDrawable
import com.tvchannels.sample.coreui.extension.getColorCompat
import com.tvchannels.sample.coreui.utils.ClickType
import com.tvchannels.sample.coreui.utils.setOnSingleClickListener
import com.tvchannels.sample.databinding.ItemChannelBinding
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class ChannelsAdapter(
    private val onItemClick: (item: ChannelItem) -> Unit,
    private val onFavClick: (id: Long) -> Unit = {}
) {

    val adapter = adapterOf<ChannelItem> {
        diff(
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new },
        )
        register(layoutResource = R.layout.item_channel, viewHolder = { v ->
            ChannelViewHolder(
                v,
                onItemClick = onItemClick,
                onFavClick = onFavClick
            )
        })
    }


    private class ChannelViewHolder(
        view: View,
        val onItemClick: (item: ChannelItem) -> Unit,
        val onFavClick: (id: Long) -> Unit = {},
    ) : RecyclerViewHolder<ChannelItem>(view) {

        private val binding = ItemChannelBinding.bind(view)

        override fun bind(position: Int, item: ChannelItem) = with(binding) {
            tvTitle.text = item.name
            tvSubtitle.text = "No Info"
            val colorFav = if(item.isFavorite) RColor.main_blue else RColor.grey_faded_mist
            ivFav.imageTintList = ColorStateList.valueOf(binding.root.context.getColorCompat(colorFav))
            ivIcon.load(item.imageUrl) {
                cachedMode(item.imageUrl)
                error(RDrawable.ic_photo_error)
            }
            ivFav.setOnSingleClickListener(clickType = ClickType.SINGLE) {
                onFavClick(item.id)
            }
            root.setOnSingleClickListener{
                onItemClick(item)
            }
        }
    }
}