package com.anil.articals.model

import com.anil.articals.di.DaggerAppComponent
import com.anil.articals.service.RetroCaller
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Anil 26/6/20
 */
class ArticalService {
    @Inject
    lateinit var api: RetroCaller


    init {
        DaggerAppComponent.create().inject(this)
    }

    suspend fun getArticals(page: String, limit: String): Response<List<ArticalListModelItem>> {
        var response = api.getArticalList(page, limit)
        return response
    }



}

