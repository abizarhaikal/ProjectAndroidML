package com.dicoding.asclepius.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.viewmodel.MainViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnItemClickCallback {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapterHistory: HistoryAdapter
    private val historyViewModel by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapterHistory = HistoryAdapter(this, this)
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.setHasFixedSize(true)
        historyViewModel.getAllCancer().observe(this) { cancerList ->
            if (cancerList != null) {
                binding.rvHistory.adapter = adapterHistory
                adapterHistory.setCancerList(cancerList)
            }
        }

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.title = "History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onItemClicked(listCancer: CancerEntity) {
        historyViewModel.delete(listCancer)
    }


}