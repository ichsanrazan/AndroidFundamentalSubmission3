package com.dicoding.androidfundamentalsubmission3.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.androidfundamentalsubmission3.ActivityDetail
import com.dicoding.androidfundamentalsubmission3.R
import com.dicoding.androidfundamentalsubmission3.adapter.AdapterSearch
import com.dicoding.androidfundamentalsubmission3.viewmodel.ViewModelFollower
import kotlinx.android.synthetic.main.fragment_follower.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentFollower : Fragment() {
    private lateinit var mViewModel: ViewModelFollower
    private lateinit var mAdapterSearch: AdapterSearch

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        recycler_follower.setHasFixedSize(true)
        recycler_follower.layoutManager = LinearLayoutManager(context)
        showRecyclerFollowers()

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                ViewModelFollower::class.java
        )

        val extraUsername = arguments?.getString(ActivityDetail.EX_USERNAME)
        if (extraUsername != null) {
            mViewModel.setFollowers(extraUsername)
            mViewModel.getFollowers().observe(this.viewLifecycleOwner, Observer { followers ->
                showLoading(false)
                if (followers != null) {
                    mAdapterSearch.setUserSearchData(followers)
                } else
                    text_follower_list_no_data.visibility = View.VISIBLE
            })
        }
    }
    private fun showRecyclerFollowers() {
        mAdapterSearch = AdapterSearch()
        mAdapterSearch.notifyDataSetChanged()
        recycler_follower.adapter = mAdapterSearch
    }
    private fun showLoading(state: Boolean) {
        if (state)
            loading_follower.visibility = View.VISIBLE
        else
            loading_follower.visibility = View.GONE
    }
}