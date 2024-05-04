package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.adapter.ArticleAdapter.Companion.DIFF_CALLBACK
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemResultBinding

class ResultAdapter : ListAdapter<ArticlesItem ,ResultAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: ArticlesItem) {
            Glide.with(binding.ivArticle)
                .load(listItem.urlToImage)
                .into(binding.ivArticle)
            if (listItem.author != null) {
                binding.tvArticleTitle.text = listItem.author
            } else {
                binding.tvArticleTitle.text = "News Updates"
            }
            binding.btnArticles.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(listItem.url)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup ,viewType: Int): ResultAdapter.MyViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context) ,parent ,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultAdapter.MyViewHolder ,position: Int) {
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