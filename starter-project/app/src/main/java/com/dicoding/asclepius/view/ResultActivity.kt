package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ResultAdapter
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.viewmodel.ResultViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var adapter: ResultAdapter
    private val resultViewModel by viewModels<ResultViewModel>()
    companion object {
        const val IMAGE_URI = "image_uri"
        const val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(IMAGE_URI)

        val textResult = intent.getStringExtra(RESULT)
        binding.resultText.text = "Hasil Prediksi : $textResult"
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)

            binding.resultImage.setImageURI(imageUri)
        } else {
            Log.e("Result Activity", "No Image URI")
        }

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.title = "Predictions"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ResultAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvResult.layoutManager = layoutManager
        binding.rvResult.setHasFixedSize(true)

        resultViewModel.listItem.observe(this) { result ->
            listView(result.take(5))
        }

        resultViewModel.snackbar.observe(this) { snackbar ->
            isLoading(snackbar)
        }

        binding.btnSeeMore.setOnClickListener {
            val intent = Intent(this@ResultActivity, ArticlesActivity::class.java)
            startActivity(intent)
        }

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
    }

    private fun isLoading(isLoading: Boolean?) {
        if (isLoading == true) {
            binding.snackBar.visibility = View.VISIBLE
        } else {
            binding.snackBar.visibility = View.GONE
        }

    }

    private fun listView(result: List<ArticlesItem>?) {
        adapter.submitList(result)
        binding.rvResult.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}