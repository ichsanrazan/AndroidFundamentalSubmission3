package com.dicoding.androidfundamentalsubmission3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.androidfundamentalsubmission3.alarm.PreferenceAlarm
import com.dicoding.androidfundamentalsubmission3.alarm.ServiceAlarm
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.act_setting.*

class ActivitySetting : AppCompatActivity() {

    private lateinit var almRem: ServiceAlarm
    private lateinit var almPref: PreferenceAlarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_setting)
        almRem = ServiceAlarm()
        almPref = PreferenceAlarm(this)
        alm_switch.isChecked = almRem.setAlarmReminder(this)
        alm_switch.setOnCheckedChangeListener {_, isChecked ->
            if (isChecked) {
                almRem.setRepeatReminder(this)
                almPref.setReminder(true)
                Snackbar.make(
                        activity_setting,
                        getString(R.string.switch_daily_on),
                        Snackbar.LENGTH_SHORT
                ).show()
            } else {
                almRem.cancelRepeatReminder(this)
                almPref.setReminder(false)
                Snackbar.make(
                        activity_setting,
                        getString(R.string.switch_daily_off),
                        Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }
}