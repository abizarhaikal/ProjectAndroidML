package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemArticlesBinding

class ArticleAdapter: ListAdapter<ArticlesItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemArticlesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: ArticlesItem) {
            Glide.with(binding.ivArticle)
                .load(listItem.urlToImage)
                .into(binding.ivArticle)
            binding.tvArticleTitle.text = listItem.title
            binding.tvArticleDescription.text = listItem.description
            binding.btnDetails.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(listItem.url)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup ,viewType: Int): MyViewHolder {
        val binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder ,position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem)

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem ,newItem: ArticlesItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ArticlesItem ,newItem: ArticlesItem): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}