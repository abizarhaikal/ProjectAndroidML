package com.dicoding.asclepius.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.repositoriy.CancerRepository

class MainViewModel(application: Application) : ViewModel() {
    private val mCancerRepository: CancerRepository = CancerRepository(application)

    fun insert(cancer: CancerEntity) {
        mCancerRepository.insert(cancer)
    }

    fun delete(cancer: CancerEntity) {
        mCancerRepository.delete(cancer)
    }

    fun getAllCancer(): LiveData<List<CancerEntity>> {
        return mCancerRepository.getCancerAll()
    }
}