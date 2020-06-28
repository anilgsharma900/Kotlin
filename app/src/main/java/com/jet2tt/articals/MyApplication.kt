package com.jet2tt.articals

import android.app.Application
import android.content.Context

/**
 * Created by Anil 28/6/20
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mContext = this@MyApplication
    }
companion object{
    private var mContext: Context? = null

    fun getContext(): Context? {
        return mContext
    }
}

}