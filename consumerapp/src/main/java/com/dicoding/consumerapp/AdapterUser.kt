package com.dicoding.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_user.view.*

class AdapterUser :
    RecyclerView.Adapter<AdapterUser.UsersViewHolder>() {

    var uData = ArrayList<UserGithub>()
        set(mUsersData) {
            if (mUsersData.size > 0) {
                this.uData.clear()
            }
            this.uData.addAll(mUsersData)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return uData.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(uData[position])
    }

    fun setUsersData(users: ArrayList<UserGithub>) {
        uData.clear()
        uData.addAll(users)
        notifyDataSetChanged()
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserGithub) {
            with(itemView) {
                Glide.with(context)
                    .load(
                        if (user.avatar != 0) {
                            user.avatar
                        } else {
                            user.avatarUrl
                        }
                    )
                    .into(circle_image_item_user_avatar)
                user_name.text = user.name
                user_username.text = user.username
                user_follower.text = user.follower.toString()
                user_following.text = user.following.toString()
            }
        }
    }
}