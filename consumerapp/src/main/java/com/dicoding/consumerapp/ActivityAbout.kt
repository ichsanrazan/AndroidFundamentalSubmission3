package com.dicoding.consumerapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ActivityAbout : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_about)

        val actionbar = supportActionBar
        actionbar!!.title = "About"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val btnLinkedin : ImageButton = findViewById(R.id.btn_linkedin)
        btnLinkedin.setOnClickListener(this)
        val btnInstagram : ImageButton = findViewById(R.id.btn_instagram)
        btnInstagram.setOnClickListener(this)
        val imgMe: ImageView = findViewById(R.id.img_item_photo)
        val tImg = R.drawable.profile_image
        Glide.with(this)
                .load(tImg)
                .apply(RequestOptions())
                .into(imgMe)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_linkedin -> {
                val checkDevProfileIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/muhammadichsanrazan/"))
                startActivity(checkDevProfileIntent)
            }
            R.id.btn_instagram -> {
                val checkDevProfileIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ichsanrazan/?hl=en"))
                startActivity(checkDevProfileIntent)
            }
        }
    }
}