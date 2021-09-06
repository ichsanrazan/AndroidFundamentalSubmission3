package com.dicoding.androidfundamentalsubmission3.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.androidfundamentalsubmission3.MainActivity
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class ViewModelFollower : ViewModel() {
    private val mDataFollower = MutableLiveData<ArrayList<UserGithub>>()
    private val mAsync = AsyncHttpClient()

    fun setFollowers(user: String) {
        val uri = Uri.parse(ViewModelDetail.BASE_URL_DET).buildUpon()
                .appendPath(user)
                .appendPath("followers")
                .build()
        val url = uri.toString()
        Log.d(MainActivity.TAG, url)
        mAsync.addHeader("Authorization", ViewModelMain.APIKEY)
        mAsync.addHeader("User-Agent", "request")
        mAsync.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?
            ) {
                try {
                    var result = ""
                    if (responseBody != null)
                        result = String(responseBody)
                    val getObject = JSONArray(result)
                    val listFollowers = ArrayList<UserGithub>()
                    for (i in 0 until getObject.length()) {
                        val item = getObject.getJSONObject(i)
                        val user = UserGithub()
                        user.avatarUrl = item.getString("avatar_url")
                        user.username = item.getString("login")
                        listFollowers.add(user)
                    }
                    mDataFollower.postValue(listFollowers)
                } catch (e: Exception) {
                    Log.d(MainActivity.TAG, e.message.toString())
                }
            }
            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                if (error != null)
                    Log.d(MainActivity.TAG, "setFollowers ${error.message}")
            }
        })
    }
    fun getFollowers(): LiveData<ArrayList<UserGithub>> {
        return mDataFollower
    }
}