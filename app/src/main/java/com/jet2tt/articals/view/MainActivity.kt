package com.jet2tt.articals.view

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jet2tt.articals.R
import com.jet2tt.articals.model.ArticalListModelItem
import com.jet2tt.articals.room.AppDatabase
import com.jet2tt.articals.util.NetManager
import com.jet2tt.articals.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ListViewModel
    private var articalAdapter = ArticalAdapter(arrayListOf())
    private var myJob: Job? = null
    var layout = LinearLayoutManager(this);
    var isLastPage: Boolean = false
    var page = 2;
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// set custom actionbar for showing title in center......or can use custom boolbar in xml and use noactionbar theam
        getSupportActionBar()!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()!!.setCustomView(R.layout.custom_actionbar);

        // start recyclerview divider
        val dividerItemDecoration = DividerItemDecoration(
            this@MainActivity,
            LinearLayoutManager.VERTICAL
        )
        ContextCompat.getDrawable(this, R.drawable.adapter_divider)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        };
        rvArticcalsList.addItemDecoration(
            dividerItemDecoration
        )
        // end recyclerview divider

        // create instance for Room database
        appDatabase = AppDatabase.getInstance(this)
        viewModel =ViewModelProvider(this).get(ListViewModel::class.java)
//        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)


        if (NetManager(this@MainActivity).isConnectedToInternet) {
            doAsync {
                appDatabase.articalDao().deleteArticalData()
                uiThread {
                    executeCoroutineToFecthData("1", "10")
                }
            }
        } else {
            retrieveLocalData()
        }

        rvArticcalsList.apply {
            layoutManager = layout
            adapter = articalAdapter
        }

        rvArticcalsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layout.itemCount
                val last = layout.findLastCompletelyVisibleItemPosition()

                if (!isLastPage && NetManager(this@MainActivity).isConnectedToInternet) {
                    if (last == totalItemCount - 1) {
                        page = (totalItemCount) / 10;
                        page++
                        executeCoroutineToFecthData(page.toString(), "10")
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        observeViewModel()
    }

    /**
     * retrieve data form room if there is no internet connection and data is exit in db
     */
    private fun retrieveLocalData() {
        var articals: List<ArticalListModelItem> = ArrayList<ArticalListModelItem>()
        doAsync {
            articals = appDatabase.articalDao().getArticalList()
            uiThread {
                if (articals.size > 0) {
                    rvArticcalsList.visibility = View.VISIBLE
                    articalAdapter.updateArtical(articals)
                    list_error.visibility = View.GONE
                } else {
                    if (list_error.visibility == View.VISIBLE) {
                        list_error.text = resources.getString(R.string.txt_no_internet)
                    }
                }
            }
        }
        loading_view.visibility = View.GONE
        internetToast()
    }


    private fun executeCoroutineToFecthData(page: String, limit: String) {
        myJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.refresh(page, limit)
        }
    }

    fun observeViewModel() {
        viewModel.articals.observe(this, Observer { articals ->
            articals?.let {
                rvArticcalsList.visibility = View.VISIBLE
                doAsync {
                    appDatabase.articalDao().insertArticalList(articals)
                    uiThread {
                    }
                }

                articalAdapter.updateArtical(articals)
                isLastPage = it.isEmpty()

            }
        })

        viewModel.articalsLoadError.observe(this, Observer { isError ->
            isError?.let {
                list_error.text = resources.getString(R.string.txt_loading_error)
                list_error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    rvArticcalsList.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        myJob?.cancel()
        super.onDestroy()
    }

    fun internetToast() {

        Toast.makeText(
            this@MainActivity,
            resources.getString(R.string.txt_no_internet),
            Toast.LENGTH_SHORT
        ).show()
    }


}

