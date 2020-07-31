package com.anil.articals.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artical")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@TypeConverters(MediaConverter::class, UserConverter::class)
data class ArticalListModelItem(
    @SerializedName("comments") val comments: Int,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt")val createdAt: String,
    @PrimaryKey
    @SerializedName("id")val id: String,
    @SerializedName("likes") var likes: Int,
    @SerializedName("media")val media: List<Media>,
    @SerializedName("user")val user: List<User>
)

