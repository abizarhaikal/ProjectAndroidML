package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.ArticleAdapter
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityArticlesBinding
import com.dicoding.asclepius.viewmodel.ArticleViewModel

class ArticlesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticlesBinding
    private val articleViewModel by viewModels<ArticleViewModel>()
    private lateinit var articlesAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articlesAdapter = ArticleAdapter()

        supportActionBar?.title = "Articles"

        val layoutManager = LinearLayoutManager(this)
        binding.rvArticle.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this ,layoutManager.orientation)
        binding.rvArticle.addItemDecoration(itemDecoration)
        binding.rvArticle.setHasFixedSize(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        articleViewModel.listItem.observe(this) { articles ->
            articles?.let {
                setListItem(it)
            }
        }

        articleViewModel.snackbar.observe(this) { snackbar ->
            setSnackbar(snackbar)
        }
    }

    private fun setSnackbar(snackbar: Boolean) {
        if (snackbar == true) {
            binding.snackBar.visibility = View.VISIBLE
        } else {
            binding.snackBar.visibility = View.GONE
        }

    }

    private fun setListItem(it: List<ArticlesItem>?) {
        articlesAdapter.submitList(it)
        binding.rvArticle.adapter = articlesAdapter

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