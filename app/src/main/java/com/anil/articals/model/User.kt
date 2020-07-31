package com.anil.articals.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.anil.articals.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("about")val about: String,
    @SerializedName("avatar")val avatar: String,
    @SerializedName("blogId")val blogId: String,
    @SerializedName("city")val city: String,
    @SerializedName("createdAt")val createdAt: String,
    @SerializedName("designation")val designation: String,
    @SerializedName("id")val id: String,
    @SerializedName("lastname")val lastname: String,
    @SerializedName("name")val name: String
){
    @BindingAdapter("avtar")
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.getContext())
            .load(imageUrl).apply(RequestOptions().circleCrop().placeholder(R.mipmap.ic_launcher))
            .into(view)
    }
}