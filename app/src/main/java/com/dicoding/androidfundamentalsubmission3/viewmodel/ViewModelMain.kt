package com.dicoding.androidfundamentalsubmission3.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.androidfundamentalsubmission3.MainActivity
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import com.dicoding.androidfundamentalsubmission3.util.AppConstant
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ViewModelMain : ViewModel() {
    companion object {
        const val BASE_URL = "https://api.github.com/search/users"
        const val QUERY_PARAM = "q"
        const val APIKEY = AppConstant.API_KEY
    }

    private val mUsersData = MutableLiveData<ArrayList<UserGithub>>()
    private val mAsync = AsyncHttpClient()

    fun setUser(user: String) {
        val uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, user)
                .build()
        val url = uri.toString()
        Log.d(MainActivity.TAG, url)
        mAsync.addHeader("Authorization", APIKEY)
        mAsync.addHeader("User-Agent", "request")
        mAsync.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                try {
                    Log.d(MainActivity.TAG, "setUser onSuccess")
                    var result = ""
                    if (responseBody != null)
                        result = String(responseBody)
                    val getObject = JSONObject(result)
                    val items = getObject.getJSONArray("items")
                    val listUsers = ArrayList<UserGithub>()
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val user = UserGithub()
                        user.avatarUrl = item.getString("avatar_url")
                        user.username = item.getString("login")
                        listUsers.add(user)
                    }
                    mUsersData.postValue(listUsers)
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
                    Log.d(MainActivity.TAG, "setUser ${error.message}")
            }
        })
    }
    fun getUser(): LiveData<ArrayList<UserGithub>> {
        return mUsersData
    }
}