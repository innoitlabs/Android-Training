package com.example.bottomnavigationviewandtablayout.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bottomnavigationviewandtablayout.fragments.TabFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    
    private val fragments = listOf(
        TabFragment.newInstance("Tab 1 Content"),
        TabFragment.newInstance("Tab 2 Content"),
        TabFragment.newInstance("Tab 3 Content")
    )

    override fun getItemCount() = fragments.size
    
    override fun createFragment(position: Int): Fragment = fragments[position]
}
