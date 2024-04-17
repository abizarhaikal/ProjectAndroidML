package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    companion object {
        const val IMAGE_URI = "image_uri"
        const val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(IMAGE_URI)
        binding.resultText.text = intent.getStringExtra(RESULT)
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)

            binding.resultImage.setImageURI(imageUri)
        } else {
            Log.e("Result Activity", "No Image URI")
        }

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
    }



}