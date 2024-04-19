package com.dicoding.asclepius.repositoriy

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.data.local.room.CancerDao
import com.dicoding.asclepius.data.local.room.CancerDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CancerRepository(application: Application) {
    private val mCancerDao : CancerDao
    private val executorsService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = CancerDatabase.getDatabase(application)
        mCancerDao = db.cancerDao()
    }

    fun getCancerAll() : LiveData<List<CancerEntity>> = mCancerDao.getAll()

    fun insert(cancer: CancerEntity) {
        executorsService.execute{mCancerDao.insert(cancer)}
    }

    fun delete(cancer: CancerEntity) {
        executorsService.execute{mCancerDao.delete(cancer)}
    }

}