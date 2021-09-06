package com.dicoding.androidfundamentalsubmission3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.androidfundamentalsubmission3.adapter.AdapterUser
import com.dicoding.androidfundamentalsubmission3.helper.MapHelper
import com.dicoding.androidfundamentalsubmission3.helper.UserHelper
import kotlinx.android.synthetic.main.act_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ActivityFavorite : AppCompatActivity() {

    private lateinit var mAdapterUser: AdapterUser
    private lateinit var uHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_favorite)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_text_title)
        invalidateOptionsMenu()

        uHelper = UserHelper.getInstance(this)
        uHelper.open()

        showRecyclerList()
        loadFavoriteUser()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == ActivityDetail.REQ_UPDATE) {
                if (resultCode == ActivityDetail.RES_DELETE) {
                    val position = data.getIntExtra(ActivityDetail.EX_POS, 0)
                    mAdapterUser.removeItem(position)
                    if (mAdapterUser.uData.size == 0)
                        favorite_nodata.visibility = View.VISIBLE
                } else if (resultCode == ActivityDetail.RES_ADD) {
                    Log.d(MainActivity.TAG, "resultCode = result add")
                }
            }
        }
    }

    private fun showRecyclerList() {
        mAdapterUser = AdapterUser()
        mAdapterUser.notifyDataSetChanged()
        recycler_favorite.setHasFixedSize(true)
        recycler_favorite.layoutManager = LinearLayoutManager(this)
        recycler_favorite.adapter = mAdapterUser
    }

    private fun loadFavoriteUser() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = uHelper.queryAll()
                MapHelper.mapUserCursorToArrayList(cursor)
            }
            val userList = deferredNotes.await()
            if (userList.size > 0) {
                mAdapterUser.setUsersData(userList)
            } else {
                favorite_nodata.visibility = View.VISIBLE
            }
        }
    }
}