package com.anil.articals.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anil.articals.model.ArticalListModelItem

/**
 * Created by Anil 28/6/20
 */
@Dao
interface ArticalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticalList(articals:List<ArticalListModelItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtical(articals:ArticalListModelItem)


    @Query("SELECT * from artical ORDER BY  cast(id as unsigned)")
    fun getArticalList():List<ArticalListModelItem>

    @Query("DELETE FROM artical")
    fun deleteArticalData()
}