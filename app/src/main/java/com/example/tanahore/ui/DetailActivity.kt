package com.example.tanahore.ui

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tanahore.data.viewmodel.DetailVM
import com.example.tanahore.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getIntExtra(DETAIL, 0)
        val bundle = Bundle()
        bundle.putInt(DETAIL, detail)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailVM::class.java]
        detailViewModel.articleDetail(detail)
        detailViewModel.detail.observe(this){
            if(it != null){
                binding.apply {
                    tvDetail.text = it.name
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        description.text = Html.fromHtml(it.description.replace("\\n", "\n"), Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        description.text = Html.fromHtml(it.description.replace("\\n", "\n"))
                    }
                    Glide.with(this@DetailActivity)
                        .load(it.image)
                        .into(binding.ivDetail)
                }
            }
        }
        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        setAction()
    }

    private fun setAction() {
        binding.back.setOnClickListener {
            finish()
        }
    }

    companion object{
        const val DETAIL = "DETAIL"
    }
}