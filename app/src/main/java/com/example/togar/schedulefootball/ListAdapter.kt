package com.example.togar.schedulefootball

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.togar.schedulefootball.main.favoritematch.FavoriteTeamsFragment
import com.example.togar.schedulefootball.main.lastmatch.LastMatchFragment
import com.example.togar.schedulefootball.main.nextmatch.NextMatchFragment

class ListAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> LastMatchFragment.newInstance()
        1 -> NextMatchFragment.newInstance()
        2 -> FavoriteTeamsFragment.FavInstance()
        else -> null
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Last Match"
        1 -> "Next Match"
        2 -> "Favorite Match"
        else -> ""
    }

    override fun getCount(): Int = 3
}