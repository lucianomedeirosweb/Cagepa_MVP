package br.ufs.projetos.gocidade.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by samila on 16/11/17.
 */
class TabsPagerAdapter : FragmentPagerAdapter {

    constructor(fm: FragmentManager?) : super(fm)

    var mFragment = ArrayList<Fragment>()

    fun addFragment (f : Fragment){
        mFragment.add(f)
    }

    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }

    override fun getCount(): Int = mFragment.size
}