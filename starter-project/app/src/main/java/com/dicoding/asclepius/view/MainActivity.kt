package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.NumberFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private lateinit var displayResults : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }



    }

    private fun startGallery() {
        launcherGalery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGalery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker" ,"No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        binding.progressIndicator.visibility = View.VISIBLE
        if(currentImageUri != null) {

            imageClassifierHelper = ImageClassifierHelper(
                context = this ,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        runOnUiThread {
                            showToast(error)
                        }
                    }
                    override fun onResult(result: List<Classifications> ,inferenceTime: Long) {
                        runOnUiThread {
                            result?.let { it ->
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    val sortedCategory =
                                        it[0].categories.sortedByDescending { it?.score }
                                     displayResults =
                                        sortedCategory.joinToString("\n") {
                                            "${it.label}" + NumberFormat.getPercentInstance()
                                                .format(it.score).trim()
                                        }
                                    showToast(displayResults)
                                    moveToResult()
                                    binding.progressIndicator.visibility = View.GONE
                                } else {
                                    showToast("error")
                                }
                            }
                        }
                    }
                }

            )
            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
        } else {
            showToast("No image selected")
        }
    }

    private fun moveToResult() {
        val intent = Intent(this ,ResultActivity::class.java)
        intent.putExtra(ResultActivity.IMAGE_URI,currentImageUri.toString())
        intent.putExtra(ResultActivity.RESULT, displayResults)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this ,message ,Toast.LENGTH_SHORT).show()
    }


}