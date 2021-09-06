package com.dicoding.consumerapp

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.DatabaseContract.UserColumns.Companion.CONTENT_URI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapterUser: AdapterUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRecycler()
        contentProvider()
        loadFavoredAsync()
    }
    private fun showRecycler() {
        recycler_consumerapp.setHasFixedSize(true)
        recycler_consumerapp.layoutManager = LinearLayoutManager(this)
        mAdapterUser = AdapterUser()
        recycler_consumerapp.adapter = mAdapterUser
    }
    private fun contentProvider() {
        val handlerThread = HandlerThread("DataMoviesObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavoredAsync()
            }
        }

        contentResolver.registerContentObserver(
            CONTENT_URI, true,
            myObserver
        )
    }

    private fun loadFavoredAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor =
                    contentResolver.query(
                        CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )
                MapHelper.mapCursorToArrayList(cursor)
            }
            val userList = deferredNotes.await()
            if (userList.size > 0) {
                text_main_no_data.visibility = View.GONE
                mAdapterUser.setUsersData(userList)
            } else {
                mAdapterUser.setUsersData(arrayListOf())
                text_main_no_data.visibility = View.VISIBLE
            }
        }
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
        }
    }
}