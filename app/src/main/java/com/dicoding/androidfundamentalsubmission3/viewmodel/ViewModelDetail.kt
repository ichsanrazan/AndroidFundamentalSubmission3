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
import org.json.JSONObject

class ViewModelDetail : ViewModel() {
    companion object {
        const val BASE_URL_DET = "https://api.github.com/users"
    }
    private val mDetailData = MutableLiveData<UserGithub>()
    private val mAsync = AsyncHttpClient()

    fun setUserDetail(username: String) {
        val uri = Uri.parse(BASE_URL_DET).buildUpon()
                .appendPath(username)
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
                    Log.d(MainActivity.TAG, "setUserDetail onSuccess")
                    var result = ""
                    if (responseBody != null)
                        result = String(responseBody)
                    val getObject = JSONObject(result)
                    val userGithub = UserGithub()
                    userGithub.name = getObject.getString("name")
                    userGithub.username = getObject.getString("login")
                    userGithub.avatarUrl = getObject.getString("avatar_url")
                    userGithub.company = getObject.getString("company")
                    userGithub.location = getObject.getString("location")
                    userGithub.repository = getObject.getString("public_repos").toInt()
                    userGithub.follower = getObject.getString("followers").toInt()
                    userGithub.following = getObject.getString("following").toInt()
                    mDetailData.postValue(userGithub)
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
                    Log.d(MainActivity.TAG, "setUserDetail ${error.message}")
            }
        })
    }

    fun getUserDetail(): LiveData<UserGithub> {
        return mDetailData
    }
}