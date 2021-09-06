package com.dicoding.androidfundamentalsubmission3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.androidfundamentalsubmission3.ActivityDetail
import com.dicoding.androidfundamentalsubmission3.R
import com.dicoding.androidfundamentalsubmission3.data.UserGithub
import kotlinx.android.synthetic.main.list_item_user.view.*

class AdapterUser : RecyclerView.Adapter<AdapterUser.UsersViewHolder>() {
    var uData = ArrayList<UserGithub>()
        set(UsersData) {
            if (UsersData.size > 0) {
                this.uData.clear()
            }
            this.uData.addAll(UsersData)
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(prent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(prent.context).inflate(R.layout.list_item_user, prent, false)
        return UsersViewHolder(view)
    }
    override fun onBindViewHolder(holder: UsersViewHolder, pos: Int) {
        holder.bind(uData[pos])
    }
    override fun getItemCount(): Int {
        return uData.size
    }
    fun setUsersData(userArray: ArrayList<UserGithub>) {
        uData.clear()
        uData.addAll(userArray)
        notifyDataSetChanged()
    }
    fun removeItem(pos: Int) {
        this.uData.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, uData.size)
    }
    fun updateItem(pos: Int, user: UserGithub) {
        uData[pos] = user
        notifyItemChanged(pos, user)
    }
    fun addItem(user: UserGithub) {
        uData.add(user)
        notifyItemInserted(uData.size - 1)
    }
    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserGithub) {
            with(itemView) {
                Glide.with(context)
                        .load(
                                if (user.avatar != 0)
                                    user.avatar
                                else
                                    user.avatarUrl
                        )
                        .into(circle_image_item_user_avatar)
                text_item_user_name.text = user.name
                text_item_user_username.text = user.username
                text_item_user_follower_count.text = user.follower.toString()
                text_item_user_following_count.text = user.following.toString()

                this.setOnClickListener {
                    val intentDetail = Intent(context, ActivityDetail::class.java)
                    intentDetail.putExtra(ActivityDetail.EX_USER, user)
                    intentDetail.putExtra(ActivityDetail.EX_POS, adapterPosition)
                    (context as Activity).startActivityForResult(
                            intentDetail,
                            ActivityDetail.REQ_UPDATE
                    )
                }
            }
        }
    }
}