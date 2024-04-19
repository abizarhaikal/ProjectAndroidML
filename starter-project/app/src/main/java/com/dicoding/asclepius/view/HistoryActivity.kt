package com.dicoding.asclepius.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

        requestPermission()
        adapterHistory = HistoryAdapter(this, this)
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.setHasFixedSize(true)
        historyViewModel.getAllCancer().observe(this) { cancerList ->
            if (cancerList != null) {
                binding.rvHistory.adapter = adapterHistory
                adapterHistory.setCancerList(cancerList)
            }

        }

    }



    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this ,"Izin diberikan" ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this ,"Izin ditolak" ,Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                this ,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this ,arrayOf(permission) ,PERMISSION_REQUEST_CODE)
        } else {
            Toast.makeText(this ,"Izin diberikan" ,Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }

    override fun onItemClicked(listCancer: CancerEntity) {
        historyViewModel.delete(listCancer)
    }


}