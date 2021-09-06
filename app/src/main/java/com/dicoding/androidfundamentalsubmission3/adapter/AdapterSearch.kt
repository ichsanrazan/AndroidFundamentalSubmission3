package com.dicoding.androidfundamentalsubmission3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.androidfundamentalsubmission3.ActivityDetail
import com.dicoding.androidfundamentalsubmission3.R
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import kotlinx.android.synthetic.main.src_item_user.view.*

class AdapterSearch : RecyclerView.Adapter<AdapterSearch.UserSearchViewHolder>() {

    private val mUserSearchData = ArrayList<UserGithub>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.src_item_user, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUserSearchData.size
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        holder.bind(mUserSearchData[position])
    }

    fun setUserSearchData(users: ArrayList<UserGithub>) {
        mUserSearchData.clear()
        mUserSearchData.addAll(users)
        notifyDataSetChanged()
    }

    class UserSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserGithub) {
            with(itemView) {
                Glide.with(context)
                        .load(user.avatarUrl)
                        .into(circle_image_item_user_search_avatar)
                text_item_user_search_username.text = user.username

                this.setOnClickListener {
                    val intentDetail = Intent(context, ActivityDetail::class.java)
                    intentDetail.putExtra(ActivityDetail.EX_USERNAME, user.username)
                    context.startActivity(intentDetail)
                }
            }
        }
    }
}