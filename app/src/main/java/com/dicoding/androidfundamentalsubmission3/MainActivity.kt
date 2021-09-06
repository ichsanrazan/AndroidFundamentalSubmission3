package com.dicoding.androidfundamentalsubmission3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.androidfundamentalsubmission3.adapter.AdapterSearch
import com.dicoding.androidfundamentalsubmission3.adapter.AdapterUser
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import com.dicoding.androidfundamentalsubmission3.viewmodel.ViewModelMain
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "hyperLoop"
    }
    private var mListUserData = arrayListOf<UserGithub>()
    private lateinit var vModelMain: ViewModelMain
    private lateinit var mAdapterUser: AdapterUser
    private lateinit var mAdapterSearch: AdapterSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vModelMain = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelMain::class.java)

        recycler_main.setHasFixedSize(true)
        recycler_main.layoutManager = LinearLayoutManager(this)
        showRecyclerUsers()

        search_main.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    showLoading(true)
                    showRecyclerUserSearch()
                    vModelMain.setUser(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    showRecyclerUsers()
                    mAdapterUser.setUsersData(mListUserData)
                }
                return false
            }
        })
        vModelMain.getUser().observe(this, Observer { users ->
            if (users != null) {
                Log.d(TAG, "getUser observe users != null")
                showLoading(false)
                mAdapterSearch.setUserSearchData(users)
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }
    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_about -> {
                val iAbout = Intent(this@MainActivity, ActivityAbout::class.java)
                startActivity(iAbout)
            }
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_favorite -> {
                val move = Intent(this, ActivityFavorite::class.java)
                startActivity(move)
            }
            R.id.action_alarm_setting -> {
                val move = Intent(this, ActivitySetting::class.java)
                startActivity(move)
            }
        }
    }
    private fun showRecyclerUsers() {
        mAdapterUser = AdapterUser()
        mAdapterUser.setUsersData(mListUserData)
        mAdapterUser.notifyDataSetChanged()
        recycler_main.adapter = mAdapterUser
    }
    private fun showRecyclerUserSearch() {
        mAdapterSearch = AdapterSearch()
        mAdapterSearch.notifyDataSetChanged()
        recycler_main.adapter = mAdapterSearch
    }
    private fun showLoading(state: Boolean) {
        if (state)
            main_loading.visibility = View.VISIBLE
        else
            main_loading.visibility = View.GONE
    }
}