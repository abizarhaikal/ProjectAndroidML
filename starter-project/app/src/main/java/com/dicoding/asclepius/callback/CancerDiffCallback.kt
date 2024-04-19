package com.dicoding.asclepius.callback

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.data.local.entity.CancerEntity

class CancerDiffCallback(private val oldCancerList: List<CancerEntity>, private val newCancerList: List<CancerEntity>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldCancerList.size
    }

    override fun getNewListSize(): Int {
        return newCancerList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int ,newItemPosition: Int): Boolean {
        return oldCancerList[oldItemPosition].id == newCancerList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int ,newItemPosition: Int): Boolean {
        val oldCancer = oldCancerList[oldItemPosition]
        val newCancer = newCancerList[newItemPosition]
        return newCancer.id == oldCancer.id
    }

}