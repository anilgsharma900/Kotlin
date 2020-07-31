package com.anil.articals.view

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.anil.articals.R
import com.anil.articals.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var bindin: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        bindin = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUi()
    }

    private fun setUi() {
        // set custom actionbar for showing title in center......or can use custom boolbar in xml and use noactionbar theam
        getSupportActionBar()!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()!!.setCustomView(R.layout.custom_actionbar);

        val articalListFragment = ArticalListFragment();
        supportFragmentManager.beginTransaction().replace(R.id.root, articalListFragment)
            .commit()

    }


}

