package com.anil.articals.viewmodel

import com.anil.articals.model.ArticalListModelItem

/**
 * Created by Anil 11/7/20
 */
interface MainNavigator {

    fun onItemClick(articalListModelItem: ArticalListModelItem);
}