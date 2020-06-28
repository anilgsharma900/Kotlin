package com.jet2tt.articals.model

import com.jet2tt.articals.di.DaggerAppComponent
import com.jet2tt.articals.service.RetroCaller
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
//        suspend fun getArticalsFromLocal(page:String,limit:String): List<ArticalListModelItem>
//    {
//        var ar:ArticalDao;

//        return  ar.getArticalList()

//    }


}

