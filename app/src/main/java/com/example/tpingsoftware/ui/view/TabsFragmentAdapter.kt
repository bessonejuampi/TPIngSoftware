package com.example.tpingsoftware.ui.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabsFragmentAdapter (fragmentManager: FragmentManager, behavior:Int): FragmentPagerAdapter(fragmentManager, behavior) {

    private val listFragment : MutableList<Fragment> = ArrayList()
    private val listTitle : MutableList<String> = ArrayList()

    fun addItem(fragment:Fragment, title:String) {
        listFragment.add(fragment)
        listTitle.add(title)
    }


    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }
}