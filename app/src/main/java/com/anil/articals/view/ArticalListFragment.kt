package com.anil.articals.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anil.articals.R
import com.anil.articals.databinding.FragmentArticalListBinding
import com.anil.articals.model.ArticalListModelItem
import com.anil.articals.room.AppDatabase
import com.anil.articals.util.NetManager
import com.anil.articals.viewmodel.ListViewModel
import com.anil.articals.viewmodel.MainNavigator
import kotlinx.android.synthetic.main.fragment_artical_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ArticalListFragment : Fragment(), MainNavigator {
    lateinit var binding: FragmentArticalListBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(ListViewModel::class.java)
    }

    private var articalAdapter = ArticalAdapter(arrayListOf())
    private var myJob: Job? = null
    var layout = LinearLayoutManager(activity);
    var isLastPage: Boolean = false
    var page = 2;
    lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_artical_list,
            container,
            false
        )

        setUi()
        // create instance for Room database
        appDatabase = AppDatabase.getInstance(activity!!)

//        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        viewModel.setMainNavigator(this)

        // check internet connecction and call netwrok api
        if (NetManager(activity!!).isConnectedToInternet) {
            doAsync {
                appDatabase.articalDao().deleteArticalData()
                uiThread {
                    executeCoroutineToFecthData("1", "10")
                }
            }
        } else {
            retrieveLocalData()
        }

        binding.rvArticcalsList.apply {
            layoutManager = layout
            adapter = articalAdapter
        }

        binding.rvArticcalsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layout.itemCount
                val last = layout.findLastCompletelyVisibleItemPosition()

                if (!isLastPage && NetManager(activity!!).isConnectedToInternet) {
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

        return binding.getRoot()

    }

    private fun setUi() {

        // start recyclerview divider
        val dividerItemDecoration = DividerItemDecoration(
            activity,
            LinearLayoutManager.VERTICAL
        )
        ContextCompat.getDrawable(activity!!, R.drawable.adapter_divider)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        binding.rvArticcalsList.addItemDecoration(
            dividerItemDecoration
        )
        // end recyclerview divider
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArticalListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ArticalListFragment()
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
                    binding.rvArticcalsList.visibility = View.VISIBLE
                    articalAdapter.updateArtical(articals, viewModel)
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
                binding.rvArticcalsList.visibility = View.VISIBLE
                doAsync {
                    appDatabase.articalDao().insertArticalList(articals)
                    uiThread {
                    }
                }
                articalAdapter.updateArtical(articals, viewModel)
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
                    binding.rvArticcalsList.visibility = View.GONE
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
            activity,
            resources.getString(R.string.txt_no_internet),
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onItemClick(articalListModelItem: ArticalListModelItem) {
        val articalDetailsFragment = ArticalDetailsFragment.newInstance();
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.root, articalDetailsFragment).addToBackStack(null).commit()

    }

}