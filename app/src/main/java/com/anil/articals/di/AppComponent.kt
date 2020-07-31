package com.anil.articals.di

import com.anil.articals.model.ArticalService
import com.anil.articals.viewmodel.ListViewModel
import dagger.Component

/**
 * Created by Anil 26/6/20
 */
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(service:ArticalService)
    fun inject(intoViewModel: ListViewModel)

}