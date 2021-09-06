package com.dicoding.androidfundamentalsubmission3

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.androidfundamentalsubmission3.adapter.AdapterFollow
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import com.dicoding.androidfundamentalsubmission3.db.DatabaseContract
import com.dicoding.androidfundamentalsubmission3.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.androidfundamentalsubmission3.fragment.FragmentFollower
import com.dicoding.androidfundamentalsubmission3.fragment.FragmentFollowing
import com.dicoding.androidfundamentalsubmission3.helper.MapHelper
import com.dicoding.androidfundamentalsubmission3.helper.UserHelper
import com.dicoding.androidfundamentalsubmission3.viewmodel.ViewModelDetail
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.act_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ActivityDetail : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EX_USER = "user"
        const val EX_USERNAME = "username"
        const val EX_POS = "pos"
        const val REQ_UPDATE = 100
        const val RES_DELETE = 101
        const val RES_ADD = 102
    }

    private lateinit var vModelMain: ViewModelDetail
    private lateinit var uHelper: UserHelper
    private lateinit var mUserGithub: UserGithub
    private lateinit var mUri: Uri
    private var mIsFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        uHelper = UserHelper.getInstance(applicationContext)
        uHelper.open()
        vModelMain = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelDetail::class.java)

        floating_favorite.setOnClickListener(this)

        showLoading(true)

        val extraUser = intent.getParcelableExtra<UserGithub>(EX_USER)
        val extraUsername = intent.getStringExtra(EX_USERNAME)

        if (extraUser != null) {
            showFollowerFollowing(extraUser.username)
            showUserDetail(extraUser)
        } else if (extraUsername != null) {
            showFollowerFollowing(extraUsername)
            vModelMain.setUserDetail(extraUsername)
            vModelMain.getUserDetail().observe(this, Observer { user ->
                if (user != null) {
                    showUserDetail(user)
                }
            })
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        uHelper.close()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.floating_favorite -> {
                val extraPosition = intent.getIntExtra(EX_POS, 0)
                val intent = Intent()
                mIsFavorite = if (mIsFavorite) {
                    uHelper.deleteByUsername(mUserGithub.username)
                    contentResolver.delete(mUri, null, null)
                    Snackbar.make(
                            v,
                            getString(R.string.snackbar_deleted),
                            Snackbar.LENGTH_SHORT
                    ).show()
                    floating_favorite.setImageResource(R.drawable.ic_favorite_border_24dp)
                    intent.putExtra(EX_USER, mUserGithub)
                    intent.putExtra(EX_POS, extraPosition)
                    setResult(RES_DELETE, intent)
                    false
                } else {
                    val values = ContentValues()
                    values.put(DatabaseContract.UserColumns.USERNAME, mUserGithub.username)
                    values.put(DatabaseContract.UserColumns.AVATAR, mUserGithub.avatar)
                    Log.d(MainActivity.TAG, "mUser.avatar = ${mUserGithub.avatar}")
                    values.put(DatabaseContract.UserColumns.AVATAR_URL, mUserGithub.avatarUrl)
                    Log.d(MainActivity.TAG, "mUser.avatarUrl = ${mUserGithub.avatarUrl}")
                    values.put(DatabaseContract.UserColumns.NAME, mUserGithub.name)
                    values.put(
                            DatabaseContract.UserColumns.REPOSITORY_COUNT,
                        mUserGithub.repository
                    )
                    values.put(DatabaseContract.UserColumns.FOLLOWER_COUNT, mUserGithub.follower)
                    values.put(
                            DatabaseContract.UserColumns.FOLLOWING_COUNT,
                        mUserGithub.following
                    )
                    values.put(DatabaseContract.UserColumns.COMPANY, mUserGithub.company)
                    values.put(DatabaseContract.UserColumns.LOCATION, mUserGithub.location)
                    contentResolver.insert(CONTENT_URI, values)

                    Snackbar.make(
                            v,
                            getString(R.string.snackbar_added),
                            Snackbar.LENGTH_SHORT
                    ).show()
                    floating_favorite.setImageResource(R.drawable.ic_favorite_24dp)
                    setResult(RES_ADD, intent)
                    true
                }
            }
        }
    }
    private fun showLoading(state: Boolean) {
        if (state)
            detail_loading.visibility = View.VISIBLE
        else
            detail_loading.visibility = View.GONE
    }
    private fun showUserDetail(userGithub: UserGithub) {
        showLoading(false)
        supportActionBar?.title = userGithub.name

        mUri = Uri.parse(CONTENT_URI.toString() + "/" + userGithub.username)

        mUserGithub = userGithub
        checkUser(userGithub.username)
        Glide.with(this)
                .load(
                        if (userGithub.avatar != 0)
                            userGithub.avatar
                        else
                            userGithub.avatarUrl
                )
                .into(circleimage_avatar)
        text_detail_name.text = userGithub.name
        detail_username.text = userGithub.username

        if (userGithub.company == "null")
            detail_company.text = "-"
        else
            detail_company.text = userGithub.company

        if (userGithub.location == "null")
            detail_location.text = "-"
        else
            detail_location.text = userGithub.location

        detail_repository_count.text = userGithub.repository.toString()
        detail_follower_count.text = userGithub.follower.toString()
        detail_following_count.text = userGithub.following.toString()
    }

    private fun showFollowerFollowing(username: String) {
        viewpager_follower_following.adapter =
                AdapterFollow(
                        this,
                        supportFragmentManager,
                        username,
                        FragmentFollower(),
                        FragmentFollowing()
                )
        tab_follower_following.setupWithViewPager(viewpager_follower_following)
    }

    private fun checkUser(user: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = uHelper.queryByUsername(user)
                MapHelper.mapUserCursorToArrayList(cursor)
            }
            val userList = deferredNotes.await()
            if (userList.size > 0) {
                floating_favorite.setImageResource(R.drawable.ic_favorite_24dp)
                mIsFavorite = true
                Log.d(MainActivity.TAG, "user saved in database")
            } else {
                Log.d(MainActivity.TAG, "user not saved in database")
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_about -> {
                val iAbout = Intent(this@ActivityDetail, ActivityAbout::class.java)
                startActivity(iAbout)
            }
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_alarm_setting -> {
                val move = Intent(this, ActivitySetting::class.java)
                startActivity(move)
            }
        }
    }
}