package com.tvchannels.sample.app.core.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

open class TvChannelsViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    private val items = mutableListOf<Fragment>()
    fun addFragment(fragment: Fragment) {
        items.add(fragment)
    }

    fun clear() {
        items.clear()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

}