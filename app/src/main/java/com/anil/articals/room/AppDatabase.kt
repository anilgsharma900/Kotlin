package com.anil.articals.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anil.articals.model.ArticalListModelItem
import com.anil.articals.model.MediaConverter
import com.anil.articals.model.UserConverter
import com.anil.articals.room.dao.ArticalDao

/**
 * Created by Anil 28/6/20
 */
@Database(entities = arrayOf(ArticalListModelItem::class),version = 1,exportSchema = false)
@TypeConverters(MediaConverter::class, UserConverter::class)
abstract class AppDatabase :RoomDatabase() {
    abstract fun articalDao(): ArticalDao
    companion object{
        @Volatile private var appDatabase:AppDatabase? = null
        fun getInstance(context: Context):AppDatabase = appDatabase?: synchronized(this){
            appDatabase?: buildDatabase(context).also { appDatabase = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context,AppDatabase::class.java,"jet2tt.db").build()
    }
}