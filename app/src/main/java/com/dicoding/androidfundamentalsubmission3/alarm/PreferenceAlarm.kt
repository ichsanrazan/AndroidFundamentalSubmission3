package com.dicoding.androidfundamentalsubmission3.alarm

import android.content.Context

class PreferenceAlarm(context: Context) {

    companion object {
        private const val PREF_NAME = "preference"
        private const val REMINDER = "reminder"
    }
    private val preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    fun setReminder(sr: Boolean) {
        val edit = preference.edit()
        edit.putBoolean(REMINDER, sr)
        edit.apply()
    }
}