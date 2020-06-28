package com.jet2tt.articals.view

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jet2tt.articals.databinding.AdapterArticalListBinding
import com.jet2tt.articals.model.ArticalListModelItem
import com.jet2tt.articals.util.Util


/**
 * Created by Anil 27/6/20
 */
class ArticalAdapter(var articalList: ArrayList<ArticalListModelItem>) :
    RecyclerView.Adapter<ArticalAdapter.ArticalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // Binding and used ViewGroup for parent's ui
        val binding = AdapterArticalListBinding.inflate(inflater, parent, false)
        return ArticalViewHolder(binding)

    }

    override fun getItemCount() = articalList.size


    override fun onBindViewHolder(holder: ArticalViewHolder, position: Int) {
        holder.bind(articalList.get(position))
    }


    class ArticalViewHolder(val binding: AdapterArticalListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         *  using Databinding in adapter so add object to ui and handle List of media and user
         */
        fun bind(artical: ArticalListModelItem) {
            binding.artical = artical
            binding.user = artical.user.get(0)

            binding.tvArticalLikes.text = Util().getFormatedNumber(artical.likes.toLong())+" Likes"
            binding.tvArticalComments.text = Util().getFormatedNumber(artical.comments.toLong())+" Comments"
//            binding.tvArticalTitle.text = Util().getRelationTime(artical.createdAt.toLong())


            // load user profile image uisng Glide with Circle shape
            artical.user.get(0).loadImage(binding.ivUserProfile, artical.user.get(0).avatar)
            if (artical.media.size == 0) {
                binding.media = null
            } else {
                binding.media = artical.media.get(0)
                // load media img uisng Glide
                artical.media.get(0).loadImage(binding.ivArticalImage, artical.media.get(0).image)

            }
            binding.executePendingBindings()

            // it will redirect user to browser for displaying URL
            Linkify.addLinks(binding.tvArticalUrl, Linkify.WEB_URLS)

        }
    }

    fun updateArtical(articalListUpdate: List<ArticalListModelItem>) {
        articalList.addAll(articalListUpdate)
        notifyDataSetChanged()
    }

}