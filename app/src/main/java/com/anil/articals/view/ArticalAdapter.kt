package com.anil.articals.view

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anil.articals.R
import com.anil.articals.databinding.AdapterArticalListBinding
import com.anil.articals.model.ArticalListModelItem
import com.anil.articals.util.Util
import com.anil.articals.viewmodel.ListViewModel
import java.util.*


/**
 * Created by Anil 27/6/20
 */
class ArticalAdapter(var articalList: ArrayList<ArticalListModelItem>) :
    RecyclerView.Adapter<ArticalAdapter.ArticalViewHolder>() {
    lateinit var listViewModel: ListViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticalViewHolder {
        val binding: AdapterArticalListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_artical_list,
            parent,
            false
        )

//        val inflater = LayoutInflater.from(parent.context)
//        val binding = AdapterArticalListBinding.inflate(inflater, parent, false)
        return ArticalViewHolder(binding)

    }

    override fun getItemCount() = articalList.size

    override fun onBindViewHolder(holder: ArticalViewHolder, position: Int) {
        holder.bind(articalList.get(position))
        holder.itemView.setOnClickListener {
            listViewModel.onItemClick(articalList.get(position))
        }
    }


    class ArticalViewHolder(val binding: AdapterArticalListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         *  using Databinding in adapter so add object to ui and handle List of media and user
         */
        fun bind(artical: ArticalListModelItem) {
            binding.artical = artical
            binding.user = artical.user.get(0)

            binding.tvArticalLikes.text =
                Util().getFormatedNumber(artical.likes.toLong()) + " Likes"
            binding.tvArticalComments.text =
                Util().getFormatedNumber(artical.comments.toLong()) + " Comments"

            // get date time in TameAgo
            binding.tvTime.text = Util().getRelationTime(artical.createdAt)

            // load user profile image uisng Glide with Circle shape
            artical.user.get(0).loadImage(binding.ivUserProfile, artical.user.get(0).avatar)
            if (artical.media.size == 0) {
                binding.media = null
            } else {
                binding.media = artical.media.get(0)
                // load media img uisng Glide
                artical.media.get(0).loadImage(binding.ivArticalImage, artical.media.get(0).image)

            }

            // it will redirect user to browser for displaying URL
            Linkify.addLinks(binding.tvArticalUrl, Linkify.WEB_URLS)
//            binding.executePendingBindings()

        }
    }

    fun updateArtical(articalListUpdate: List<ArticalListModelItem>, viewModel: ListViewModel) {
        articalList.addAll(articalListUpdate)
        listViewModel = viewModel
        notifyDataSetChanged()
    }



}