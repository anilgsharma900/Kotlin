package com.anil.articals.model

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * Created by Anil 28/6/20
 */
class UserConverter {
    @TypeConverter
    fun listToJson(values: List<User>?) = Gson().toJson(values)

    @TypeConverter
    fun jsonFromList(values: String) = Gson().fromJson(values, Array<User>::class.java).toList()
}