package com.jet2tt.articals.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jet2tt.articals.di.DaggerAppComponent
import com.jet2tt.articals.model.ArticalListModelItem
import com.jet2tt.articals.model.ArticalService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Anil 26/6/20
 */
class ListViewModel:ViewModel() {
    @Inject
    lateinit var articalService: ArticalService

    init {
        DaggerAppComponent.create().inject(this)
    }

    val articals = MutableLiveData<List<ArticalListModelItem>>()
    val articalsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    fun refresh(page:String,limit:String) {
        viewModelScope.launch(Dispatchers.Main) {
            loading.value =true
            fetchListOfArticalsWithCoroutines(page,limit)
        }
    }


    private suspend fun fetchListOfArticalsWithCoroutines(page:String,limit:String)
    {
        viewModelScope.launch {
            try {
                val result = articalService.getArticals(page,limit)
                if(result.isSuccessful)
                {
                    articals.value = result.body()
                    articalsLoadError.value = false
                    loading.value= false
                }
                else
                {
                    articalsLoadError.value = true
                    loading.value = false
                }
            }
            catch (e:Exception)
            {
                // handle the error
                articalsLoadError.value = true
                loading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }




}


