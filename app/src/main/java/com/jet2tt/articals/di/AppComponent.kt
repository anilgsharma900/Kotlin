package com.jet2tt.articals.di

import com.jet2tt.articals.model.ArticalService
import com.jet2tt.articals.viewmodel.ListViewModel
import dagger.Component

/**
 * Created by Anil 26/6/20
 */
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(service:ArticalService)
    fun inject(intoViewModel: ListViewModel)

}