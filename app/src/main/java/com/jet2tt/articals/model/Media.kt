package com.jet2tt.articals.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName


data class Media(
    @SerializedName("blogId")val blogId: String,
    @SerializedName("createdAt")val createdAt: String,
    @SerializedName("id")val id: String,
    @SerializedName("image")val image: String,
    @SerializedName("title")val title: String,
    @SerializedName("url")val url: String
){
    @BindingAdapter("mediaImage")
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.getContext())
            .load(imageUrl)
            .apply { RequestOptions.noAnimation() }
            .into(view)
    }
}