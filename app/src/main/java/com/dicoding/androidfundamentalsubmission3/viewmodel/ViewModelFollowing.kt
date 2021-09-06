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

class ViewModelFollowing : ViewModel() {
    private val mDataFollowing = MutableLiveData<ArrayList<UserGithub>>()
    private val mAsync = AsyncHttpClient()

    fun setFollowing(user: String) {
        val uri = Uri.parse(ViewModelDetail.BASE_URL_DET).buildUpon()
                .appendPath(user)
                .appendPath("following")
                .build()
        val url = uri.toString()
        Log.d(MainActivity.TAG, url)
        mAsync.addHeader("Authorization", ViewModelMain.APIKEY)
        mAsync.addHeader("User-Agent", "request")
        mAsync.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                try {
                    var result = ""
                    if (responseBody != null)
                        result = String(responseBody)
                    val getObject = JSONArray(result)
                    val listFollowing = ArrayList<UserGithub>()
                    for (i in 0 until getObject.length()) {
                        val item = getObject.getJSONObject(i)
                        val user = UserGithub()
                        user.avatarUrl = item.getString("avatar_url")
                        user.username = item.getString("login")
                        listFollowing.add(user)
                    }
                    mDataFollowing.postValue(listFollowing)
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
                    Log.d(MainActivity.TAG, "setFollowing ${error.message}")
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<UserGithub>> {
        return mDataFollowing
    }
}