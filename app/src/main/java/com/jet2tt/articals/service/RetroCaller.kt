package com.jet2tt.articals.service

import com.jet2tt.articals.model.ArticalListModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Anil 26/6/20
 */
interface RetroCaller {

    @GET("blogs")
    suspend fun getArticalList(@Query("page") pageNumber: String,
                        @Query("limit") limit: String): Response<List<ArticalListModelItem>>

}