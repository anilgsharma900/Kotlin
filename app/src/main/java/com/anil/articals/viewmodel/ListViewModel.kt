package com.anil.articals.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anil.articals.di.DaggerAppComponent
import com.anil.articals.model.ArticalListModelItem
import com.anil.articals.model.ArticalService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Anil 26/6/20
 */
class ListViewModel : ViewModel() {
    @Inject
    lateinit var articalService: ArticalService

    init {
        DaggerAppComponent.create().inject(this)
//        DaggerAppComponent.builder().apiModule(ApiModule()).build().inject(this)
//        DaggerAppComponent.builder().apiModule(ApiModule()).build()
    }

    val articals = MutableLiveData<List<ArticalListModelItem>>()
    val articalsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
//    var mainNavigator = null

    val selected = MutableLiveData<ArticalListModelItem>()
    fun select(articalListModelItem: ArticalListModelItem) {
        selected.value = articalListModelItem;
    }

    lateinit var navigator: MainNavigator
    fun setMainNavigator(mainNavigator: MainNavigator) {
        navigator = mainNavigator
    }

    fun refresh(page: String, limit: String) {
        viewModelScope.launch(Dispatchers.Main) {
            loading.value = true
            fetchListOfArticalsWithCoroutines(page, limit)
        }
    }


    private suspend fun fetchListOfArticalsWithCoroutines(page: String, limit: String) {
        // launcch executing on worker thread
        viewModelScope.launch {
            try {
                val result = articalService.getArticals(page, limit)
                if (result.isSuccessful) {
                    articals.value = result.body()
                    articalsLoadError.value = false
                    loading.value = false
                } else {
                    articalsLoadError.value = true
                    loading.value = false
                }
            } catch (e: Exception) {
                // handle the error
                articalsLoadError.value = true
                loading.value = false
            }
        }
    }


    fun onItemClick(articalListModelItem: ArticalListModelItem) {
        navigator.onItemClick(articalListModelItem)
    }

    override fun onCleared() {
        super.onCleared()
    }


}


