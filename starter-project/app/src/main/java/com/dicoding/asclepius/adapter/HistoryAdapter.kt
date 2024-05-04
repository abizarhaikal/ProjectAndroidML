package com.dicoding.asclepius.adapter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.callback.CancerDiffCallback
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import kotlin.coroutines.coroutineContext

class HistoryAdapter(private val context: Context, private val itemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val listCancer = ArrayList<CancerEntity>()
    fun setCancerList(listCancer: List<CancerEntity>) {
        val diffCallback = CancerDiffCallback(this.listCancer ,listCancer)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listCancer.clear()
        this.listCancer.addAll(listCancer)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.btnDelete.setOnClickListener {
                    val position = adapterPosition
                    val cancer = listCancer[position]
                    itemClickCallback.onItemClicked(cancer)
                }
            }
        fun bind(cancer: CancerEntity) {
            with(binding) {
                tvCancer.text = cancer.cancer
                Glide.with(context)
                    .load(Uri.parse(cancer.image))
                    .into(ivCancer)
            }
        }

    }



    override fun onCreateViewHolder(
        parent: ViewGroup ,
        viewType: Int
    ): HistoryAdapter.HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context) ,parent ,false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder ,position: Int) {
        holder.bind(listCancer[position])
    }

    override fun getItemCount(): Int {
        return listCancer.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(listCancer: CancerEntity)
    }
}