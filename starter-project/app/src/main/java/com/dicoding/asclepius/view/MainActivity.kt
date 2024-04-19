package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import androidx.activity.viewModels
import android.icu.text.NumberFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.CancerEntity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.MainViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private lateinit var displayResults : String

    private val mainViewModel by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
        binding.clearButton.setOnClickListener { clearImage()}

        if(currentImageUri == null) {
            binding.clearButton.isEnabled = false
        }


    }

    private fun clearImage() {
        if (currentImageUri != null) {
            currentImageUri = null
            binding.previewImageView.setImageURI(null)
            binding.previewImageView.setImageDrawable(getDrawable(R.drawable.upload_image_ui))
            binding.galleryButton.isEnabled = true
            binding.clearButton.isEnabled = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun startGallery() {
        launcherGalery.launch("image/*")
    }

    private val launcherGalery = registerForActivityResult(
        ActivityResultContracts.GetContent()
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
        if (currentImageUri != null) {
            binding.galleryButton.isEnabled = false
            binding.clearButton.isEnabled = true
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
        val cancerEntity = CancerEntity(
            id = 0,
            displayResults,
            currentImageUri.toString())
        mainViewModel.insert(cancerEntity)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this ,message ,Toast.LENGTH_SHORT).show()
    }


}