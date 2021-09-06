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
import com.dicoding.androidfundamentalsubmission3.viewmodel.ViewModelFollowing
import kotlinx.android.synthetic.main.fragment_following.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentFollowing : Fragment() {

    private lateinit var mViewModel: ViewModelFollowing
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
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        recycler_following.setHasFixedSize(true)
        recycler_following.layoutManager = LinearLayoutManager(context)
        showRecyclerFollowing()

        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                ViewModelFollowing::class.java
        )

        val extraUsername = arguments?.getString(ActivityDetail.EX_USERNAME)
        if (extraUsername != null) {
            mViewModel.setFollowing(extraUsername)
            mViewModel.getFollowing()
                    .observe(this.viewLifecycleOwner, Observer { following ->
                        showLoading(false)
                        if (following != null) {
                            mAdapterSearch.setUserSearchData(following)
                        } else
                            text_following_list_no_data.visibility = View.VISIBLE
                    })
        }
    }

    private fun showRecyclerFollowing() {
        mAdapterSearch = AdapterSearch()
        mAdapterSearch.notifyDataSetChanged()
        recycler_following.adapter = mAdapterSearch
    }

    private fun showLoading(state: Boolean) {
        if (state)
            following_loading.visibility = View.VISIBLE
        else
            following_loading.visibility = View.GONE
    }
}