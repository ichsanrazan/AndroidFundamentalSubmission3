package com.dicoding.androidfundamentalsubmission3.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.androidfundamentalsubmission3.ActivityDetail
import com.dicoding.androidfundamentalsubmission3.R
import com.dicoding.androidfundamentalsubmission3.fragment.FragmentFollower
import com.dicoding.androidfundamentalsubmission3.fragment.FragmentFollowing

class AdapterFollow(
        private val cText: Context,
        mFragmentManager: FragmentManager,
        private val Uname: String,
        private val FollowerFrag: FragmentFollower,
        private val FollowingFrag: FragmentFollowing) :
        FragmentPagerAdapter(mFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mTabTitles =
            intArrayOf(R.string.text_follower,
                R.string.text_following)

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(ActivityDetail.EX_USERNAME, Uname)
        FollowerFrag.arguments = bundle
        FollowingFrag.arguments = bundle
        when (position) {
            0 -> return FollowerFrag
            1 -> return FollowingFrag
        }
        return FollowerFrag
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return cText.resources.getString(mTabTitles[position])
    }
    override fun getCount(): Int {
        return 2
    }
}